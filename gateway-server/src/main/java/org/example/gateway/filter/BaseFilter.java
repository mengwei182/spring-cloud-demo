package org.example.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.CaffeineRedisCache;
import org.example.common.entity.base.Token;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.entity.system.vo.UserVo;
import org.example.common.model.CommonResult;
import org.example.common.util.GsonUtils;
import org.example.common.util.TokenUtils;
import org.example.properties.CommonProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
    public static final String AUTHORIZATION = "Authorization";
    private static final String USER_TOKEN_KEY = "USER_TOKEN_KEY_";
    @Resource
    private CommonProperties commonProperties;
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
        byte[] bytes = GsonUtils.gson().toJson(CommonResult.unauthorized()).getBytes();
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        // 校验请求中的token参数和数据
        String authorization = authorizationHeaderFilter(request);
        if (StrUtil.isEmpty(authorization)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            return response.writeWith(Mono.just(dataBuffer));
        }
        Token<UserVo> token = TokenUtils.unsigned(authorization, UserVo.class);
        UserVo userVo = token.getData();
        // 校验token
        if (!tokenFilter(authorization, userVo)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            return response.writeWith(Mono.just(dataBuffer));
        }
        // 校验资源
        if (!resourceFilter(userVo, request)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            return response.writeWith(Mono.just(dataBuffer));
        }
        return chain.filter(exchange);
    }

    private boolean urlWhiteFilter(ServerHttpRequest request) {
        String path = request.getPath().value();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 校验是否是不需要验证token的url
        Optional<String> first = commonProperties.getSkipUrl().stream().filter(o -> antPathMatcher.match(o, path)).findFirst();
        return first.isPresent();
    }

    private String authorizationHeaderFilter(ServerHttpRequest request) {
        // 校验请求中的token参数和数据
        String authorizationHeader = request.getHeaders().getFirst(AUTHORIZATION);
        String authorizationParameter = request.getQueryParams().getFirst(AUTHORIZATION);
        return !StrUtil.isEmpty(authorizationHeader) ? authorizationHeader : authorizationParameter;
    }

    private boolean tokenFilter(String authorization, UserVo userVo) {
        // 校验请求中的token参数和数据
        if (userVo == null) {
            return false;
        }
        try {
            // token过期
            String token = caffeineRedisCache.get(USER_TOKEN_KEY + userVo.getId(), String.class);
            return !StrUtil.isEmpty(token) && authorization.equals(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean resourceFilter(UserVo userVo, ServerHttpRequest request) {
        String path = request.getPath().value();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<ResourceVo> resourceVos = userVo.getResources();
        Optional<ResourceVo> findAny = resourceVos.stream().filter(o -> antPathMatcher.match(o.getUrl(), path)).findAny();
        return findAny.isPresent();
    }
}