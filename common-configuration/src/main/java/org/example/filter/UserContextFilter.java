package org.example.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.usercontext.UserContext;
import org.example.common.util.TokenUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
@WebFilter
@Component
@Order(Integer.MIN_VALUE)
public class UserContextFilter implements Filter {
    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String authorizationParameter = request.getParameter(AUTHORIZATION);
        String authorization = !StrUtil.isEmpty(authorizationHeader) ? authorizationHeader : authorizationParameter;
        try {
            UserContext.set(TokenUtils.unsigned(authorization, UserInfoVo.class).getData());
        } catch (Exception e) {
            log.info("request is white list or authorization invalid. url:{}, authorization:{}", request.getRequestURL().toString(), authorization);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}