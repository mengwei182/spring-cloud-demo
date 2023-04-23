package org.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.properties.CommonProperties;
import org.example.common.util.TokenUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(2)
@Component
public class ResourceFilter implements GlobalFilter {
    @Resource
    private CommonProperties commonProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 简单请求直接放行
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String path = request.getPath().value();
        if (!StringUtils.hasLength(path)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 校验是否是不需要验证token的url
        Optional<String> first = Arrays.stream(commonProperties.getUrlWhiteList().split(",")).filter(noAuthUrl -> antPathMatcher.match(noAuthUrl, path)).findFirst();
        if (first.isPresent()) {
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }
        List<String> cookies = request.getHeaders().get("Authorization");
        if (CollectionUtils.isEmpty(cookies) || !StringUtils.hasLength(cookies.get(0))) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String cookie = cookies.get(0);
        UserInfoVo userInfoVo = (UserInfoVo) TokenUtils.unsigned(cookie).getData();
        List<ResourceVo> resourceVos = userInfoVo.getResources();
        Optional<ResourceVo> findAny = resourceVos.stream().filter(o -> antPathMatcher.match(o.getUrl(), path)).findAny();
        if (findAny.isPresent()) {
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}