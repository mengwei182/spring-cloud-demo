package org.example.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.example.CaffeineRedisCache;
import org.example.common.core.domain.LoginUser;
import org.example.common.core.domain.Token;
import org.example.common.core.result.CommonResult;
import org.example.common.core.result.CommonServerResult;
import org.example.common.core.result.SystemServerResult;
import org.example.common.core.util.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 基础过滤器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class BaseFilter implements GlobalFilter {
    @Value("${skip-urls}")
    @NacosValue(value = "${skip-urls}", autoRefreshed = true)
    private String skipUrls;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 简单请求直接放行
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        // 校验是否是不需要验证token的url
        if (urlWhiteFilter(request)) {
            return chain.filter(exchange);
        }
        // 校验请求中的token参数和数据
        String authorization = authorizationHeaderFilter(request);
        if (StrUtil.isEmpty(authorization)) {
            return writeUnauthorized(response);
        }
        Token<LoginUser> token;
        try {
            token = TokenUtils.unsigned(authorization, LoginUser.class);
        } catch (Exception e) {
            return writeUnauthorized(response);
        }
        LoginUser loginUser = token.getData();
        // 校验token
        if (!tokenFilter(authorization, loginUser.getId())) {
            return writeUnauthorized(response);
        }
        // 校验资源
        if (!resourceFilter(request.getPath().value(), loginUser.getResourceUrls())) {
            return writeUnauthorized(response);
        }
        return chain.filter(exchange);
    }

    private boolean urlWhiteFilter(ServerHttpRequest request) {
        String path = request.getPath().value();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 校验是否是不需要验证token的url
        return Arrays.stream(skipUrls.split(",")).anyMatch(o -> antPathMatcher.match(o, path));
    }

    private String authorizationHeaderFilter(ServerHttpRequest request) {
        // 校验请求中的token参数和数据
        String authorizationHeader = request.getHeaders().getFirst(CommonServerResult.AUTHORIZATION);
        String authorizationParameter = request.getQueryParams().getFirst(CommonServerResult.AUTHORIZATION);
        return !StrUtil.isEmpty(authorizationHeader) ? authorizationHeader : authorizationParameter;
    }

    private boolean tokenFilter(String authorization, String userId) {
        // 校验请求中的token参数和数据
        if (StrUtil.isEmpty(userId)) {
            return false;
        }
        try {
            // token是否有效
            String tokenString = caffeineRedisCache.get(SystemServerResult.USER_TOKEN_KEY + userId, String.class);
            boolean tokenValid = !StrUtil.isEmpty(tokenString) && authorization.equals(tokenString);
            // token是否过期
            Token<?> token = TokenUtils.unsigned(authorization);
            Date currentDate = new Date();
            Date expirationDate = token.getExpirationDate();
            boolean tokenExpiration = expirationDate.getTime() <= currentDate.getTime();
            return tokenValid && tokenExpiration;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean resourceFilter(String servletPath, List<String> resourceUrls) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return resourceUrls.stream().anyMatch(o -> antPathMatcher.match(o, servletPath));
    }

    private Mono<Void> writeUnauthorized(ServerHttpResponse response){
        byte[] bytes = JSON.toJSONString(CommonResult.unauthorized()).getBytes();
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(dataBuffer));
    }
}