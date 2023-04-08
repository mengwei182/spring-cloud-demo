package org.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.entity.system.vo.RoleResourceRelationVo;
import org.example.common.entity.system.vo.RoleVo;
import org.example.common.properties.CommonProperties;
import org.example.common.util.TokenUtils;
import org.example.dubbo.userserver.ResourceDubboService;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.dubbo.userserver.RoleResourceRelationDubboService;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(2)
@Component
public class ResourceFilter implements GlobalFilter {
    @DubboReference
    private RoleDubboService roleDubboService;
    @DubboReference
    private ResourceDubboService resourceDubboService;
    @Resource
    private CommonProperties commonProperties;
    @DubboReference
    private RoleResourceRelationDubboService roleResourceRelationDubboService;

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
        if (StringUtils.hasLength(path)) {
            // 校验是否是不需要验证token的url
            String[] noAuthUrls = commonProperties.getUrlWhiteList().split(",");
            for (String noAuthUrl : noAuthUrls) {
                if (antPathMatcher.match(noAuthUrl, path)) {
                    response.setStatusCode(HttpStatus.OK);
                    return chain.filter(exchange);
                }
            }
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        List<String> cookies = request.getHeaders().get("Cookie");
        if (CollectionUtils.isEmpty(cookies) || !StringUtils.hasLength(cookies.get(0))) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String cookie = cookies.get(0);
        TokenVo<?> tokenVo = TokenUtils.unsigned(cookie);
        String userId = (String) tokenVo.getId();
        List<RoleVo> roleVos = roleDubboService.getRoleByUserId(userId);
        Map<String, RoleVo> roleMap = roleVos.stream().collect(Collectors.toMap(RoleVo::getId, o -> o));
        List<ResourceVo> resourceVos = resourceDubboService.getResources();
        Map<String, ResourceVo> resourceMap = resourceVos.stream().collect(Collectors.toMap(ResourceVo::getId, o -> o));
        List<RoleResourceRelationVo> roleResourceRelationVos = roleResourceRelationDubboService.getRoleResourceRelations();
        for (RoleResourceRelationVo roleResourceRelationVo : roleResourceRelationVos) {
            RoleVo roleVo = roleMap.get(roleResourceRelationVo.getRoleId());
            if (roleVo != null) {
                ResourceVo resourceVo = resourceMap.get(roleResourceRelationVo.getResourceId());
                if (antPathMatcher.match(resourceVo.getUrl(), path)) {
                    response.setStatusCode(HttpStatus.OK);
                    return chain.filter(exchange);
                }
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}