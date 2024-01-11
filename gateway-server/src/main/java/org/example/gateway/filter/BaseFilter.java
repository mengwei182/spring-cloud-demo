package org.example.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.Token;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.vo.ResourceVo;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
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
    @Resource
    private CommonProperties commonProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public static final String AUTHORIZATION = "Authorization";
    private static final String USER_TOKEN_KEY = "USER_TOKEN_KEY_";

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
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        Token<UserInfoVo> token = TokenUtils.unsigned(authorization, UserInfoVo.class);
        UserInfoVo userInfoVo = token.getData();
        // 校验token
        if (!tokenFilter(authorization, userInfoVo)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        // 校验资源
        if (!resourceFilter(userInfoVo, request)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete().onErrorComplete();
        }
        return chain.filter(exchange);
    }

    private boolean urlWhiteFilter(ServerHttpRequest request) {
        String path = request.getPath().value();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 校验是否是不需要验证token的url
        Optional<String> first = Arrays.stream(commonProperties.getSkipUrl().split(",")).filter(o -> antPathMatcher.match(o, path)).findFirst();
        return first.isPresent();
    }

    private String authorizationHeaderFilter(ServerHttpRequest request) {
        // 校验请求中的token参数和数据
        String authorizationHeader = request.getHeaders().getFirst(AUTHORIZATION);
        String authorizationParameter = request.getQueryParams().getFirst(AUTHORIZATION);
        return !StrUtil.isEmpty(authorizationHeader) ? authorizationHeader : authorizationParameter;
    }

    private boolean tokenFilter(String authorization, UserInfoVo userInfoVo) {
        // 校验请求中的token参数和数据
        if (userInfoVo == null) {
            return false;
        }
        try {
            // token过期
            String token = (String) redisTemplate.opsForValue().get(USER_TOKEN_KEY + userInfoVo.getId());
            return !StrUtil.isEmpty(token) && authorization.equals(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean resourceFilter(UserInfoVo userInfoVo, ServerHttpRequest request) {
        String path = request.getPath().value();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<ResourceVo> resourceVos = userInfoVo.getResources();
        Optional<ResourceVo> findAny = resourceVos.stream().filter(o -> antPathMatcher.match(o.getUrl(), path)).findAny();
        return findAny.isPresent();
    }
}