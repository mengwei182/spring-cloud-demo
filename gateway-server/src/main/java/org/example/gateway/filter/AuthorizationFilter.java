package org.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * token拦截器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(1)
@Component
public class AuthorizationFilter implements GlobalFilter {
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
        String path = request.getPath().value();
        if (!StringUtils.hasLength(path)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 校验是否是不需要验证token的url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Optional<String> first = Arrays.stream(commonProperties.getUrlWhiteList().split(",")).filter(noAuthUrl -> antPathMatcher.match(noAuthUrl, path)).findFirst();
        if (first.isPresent()) {
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }
        List<String> cookies = request.getHeaders().get("Cookie");
        if (CollectionUtils.isEmpty(cookies) || !StringUtils.hasLength(cookies.get(0))) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String cookie = cookies.get(0);
        TokenVo<?> tokenVo = TokenUtils.unsigned(cookie);
        if (tokenVo != null) {
            if (tokenVo.getSignTime().getTime() + tokenVo.getExpiration() * 1000 >= new Date().getTime()) {
                response.setStatusCode(HttpStatus.OK);
                return chain.filter(exchange);
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}