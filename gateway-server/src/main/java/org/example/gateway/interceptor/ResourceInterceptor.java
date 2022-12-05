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
import org.example.dubbo.userserver.entity.ResourceDubboVo;
import org.example.dubbo.userserver.entity.RoleDubboVo;
import org.example.dubbo.userserver.entity.RoleResourceRelationDubboVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
    @Resource
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
        List<RoleDubboVo> roleDubboVos = roleDubboService.getRoleByUserId(userId);
        Map<String, RoleDubboVo> roleMap = roleDubboVos.stream().collect(Collectors.toMap(RoleDubboVo::getId, o -> o));
        List<ResourceDubboVo> resourceDubboVos = resourceDubboService.getResources();
        Map<String, ResourceDubboVo> resourceMap = resourceDubboVos.stream().collect(Collectors.toMap(ResourceDubboVo::getId, o -> o));
        List<RoleResourceRelationDubboVo> roleResourceRelationDubboVos = roleResourceRelationDubboService.getRoleResourceRelations();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (RoleResourceRelationDubboVo roleResourceRelationDubboVo : roleResourceRelationDubboVos) {
            RoleDubboVo roleDubboVo = roleMap.get(roleResourceRelationDubboVo.getRoleId());
            if (roleDubboVo != null) {
                ResourceDubboVo resourceDubboVo = resourceMap.get(roleResourceRelationDubboVo.getResourceId());
                if (antPathMatcher.match(resourceDubboVo.getUrl(), servletPath)) {
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