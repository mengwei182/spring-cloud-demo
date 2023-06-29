package org.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.util.TokenUtils;
import org.example.properties.CommonProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

/**
 * 权限校验拦截器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(1)
@Component
public class TokenFilter implements GlobalFilter {
    @Resource
    private CommonProperties commonProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
            return response.setComplete().onErrorComplete();
        }
        // 校验是否是不需要验证token的url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Optional<String> first = Arrays.stream(commonProperties.getUrlWhiteList().split(",")).filter(noAuthUrl -> antPathMatcher.match(noAuthUrl, path)).findFirst();
        if (first.isPresent()) {
            response.setStatusCode(HttpStatus.OK);
            return chain.filter(exchange);
        }
        // 校验请求中的token参数和数据
        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        String authorizationParameter = request.getQueryParams().getFirst("Authorization");
        String authorization = StringUtils.hasLength(authorizationHeader) ? authorizationHeader : authorizationParameter;
        if (!StringUtils.hasLength(authorization)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        TokenVo<UserInfoVo> tokenVo = TokenUtils.unsigned(authorization, UserInfoVo.class);
        UserInfoVo userInfoVo = tokenVo.getData();
        if (userInfoVo == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        // token过期
        Object token = redisTemplate.opsForValue().get(userInfoVo.getId());
        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        return chain.filter(exchange);
    }
}