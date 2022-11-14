package org.example.gateway.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.common.entity.vo.TokenVo;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.properties.ConfigProperties;
import org.example.common.util.TokenUtil;
import org.example.dubbo.userserver.ResourceDubboService;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.dubbo.userserver.RoleResourceRelationDubboService;
import org.example.dubbo.userserver.entity.Resource;
import org.example.dubbo.userserver.entity.Role;
import org.example.dubbo.userserver.entity.RoleResourceRelation;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class ResourceInterceptor implements HandlerInterceptor {
    @javax.annotation.Resource
    private ConfigProperties configProperties;
    @DubboReference
    private RoleDubboService roleDubboService;
    @DubboReference
    private ResourceDubboService resourceDubboService;
    @DubboReference
    private RoleResourceRelationDubboService roleResourceRelationDubboService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 简单请求直接放行
        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String servletPath = request.getServletPath();
        if (StringUtils.hasLength(servletPath)) {
            // 校验是否是不需要验证token的url
            String[] noAuthUrls = configProperties.getNoAuthUrls().split(",");
            for (String noAuthUrl : noAuthUrls) {
                if (servletPath.startsWith(noAuthUrl)) {
                    return true;
                }
            }
        } else {
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }
        String cookie = request.getHeader("Cookie");
        TokenVo<?> tokenVo = TokenUtil.unsigned(cookie);
        String userId = (String) tokenVo.getId();
        List<Role> roles = roleDubboService.getRoleByUserId(userId);
        Map<String, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, o -> o));
        List<Resource> resources = resourceDubboService.getResources();
        Map<String, Resource> resourceMap = resources.stream().collect(Collectors.toMap(Resource::getId, o -> o));
        List<RoleResourceRelation> roleResourceRelations = roleResourceRelationDubboService.getRoleResourceRelations();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (RoleResourceRelation roleResourceRelation : roleResourceRelations) {
            Role role = roleMap.get(roleResourceRelation.getRoleId());
            if (role != null) {
                Resource resource = resourceMap.get(roleResourceRelation.getResourceId());
                if (antPathMatcher.match(resource.getUrl(), servletPath)) {
                    return true;
                }
            }
        }
        throw new CommonException(CommonErrorResult.UNAUTHORIZED);
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
    }
}