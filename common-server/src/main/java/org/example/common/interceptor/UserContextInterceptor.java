package org.example.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.vo.TokenVo;
import org.example.common.entity.vo.UserInfoVo;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.properties.ConfigProperties;
import org.example.common.usercontext.UserContext;
import org.example.common.util.TokenUtil;
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

/**
 * UserContext拦截器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(3)
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Resource
    private ConfigProperties configProperties;

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
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            String[] noAuthUrls = configProperties.getNoAuthUrls().split(",");
            for (String noAuthUrl : noAuthUrls) {
                if (antPathMatcher.match(noAuthUrl, servletPath)) {
                    return true;
                }
            }
        } else {
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }
        String cookie = request.getHeader("Cookie");
        TokenVo<UserInfoVo> tokenVo = TokenUtil.unsigned(cookie, UserInfoVo.class);
        // 经过AuthorizationInterceptor拦截器处理后，token不会为null
        UserInfoVo userInfoVo = tokenVo.getData();
        UserContext.set(userInfoVo.getId(), userInfoVo.getUsername(), userInfoVo);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
    }
}