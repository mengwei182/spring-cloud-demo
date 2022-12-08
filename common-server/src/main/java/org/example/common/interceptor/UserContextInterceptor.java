package org.example.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.vo.TokenVo;
import org.example.common.entity.vo.UserInfoVo;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.properties.ConfigProperties;
import org.example.common.usercontext.UserContext;
import org.example.common.util.TokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UserContext拦截器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order(3)
@WebFilter
public class UserContextInterceptor implements Filter {
    @Resource
    private ConfigProperties configProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 简单请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String servletPath = request.getServletPath();
        if (StringUtils.hasLength(servletPath)) {
            // 校验是否是不需要验证token的url
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            String[] noAuthUrls = configProperties.getNoAuthUrls().split(",");
            for (String noAuthUrl : noAuthUrls) {
                if (antPathMatcher.match(noAuthUrl, servletPath)) {
                    return;
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
    }
}