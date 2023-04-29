package org.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.TokenUtils;
import org.example.properties.CommonProperties;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

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
    private CommonProperties commonProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 简单请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String servletPath = request.getServletPath();
        if (!StringUtils.hasLength(servletPath)) {
            throw new CommonException(CommonErrorResult.UNAUTHORIZED);
        }
        // 校验是否是不需要验证token的url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Optional<String> first = Arrays.stream(commonProperties.getUrlWhiteList().split(",")).filter(noAuthUrl -> antPathMatcher.match(noAuthUrl, servletPath)).findFirst();
        if (first.isPresent()) {
            return;
        }
        String cookie = request.getHeader("Cookie");
        TokenVo<UserInfoVo> tokenVo = TokenUtils.unsigned(cookie, UserInfoVo.class);
        // 经过AuthorizationInterceptor拦截器处理后，token不会为null
        UserInfoVo userInfoVo = tokenVo.getData();
        UserContext.set(userInfoVo.getId(), userInfoVo.getUsername(), userInfoVo);
    }
}