package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.global.ResultCode;
import org.example.common.model.CommonResult;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.TokenUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
@WebFilter
@Component
@Order(Integer.MIN_VALUE)
public class UserContextFilter implements Filter {
    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String authorizationParameter = request.getParameter(AUTHORIZATION);
        String authorization = StringUtils.hasLength(authorizationHeader) ? authorizationHeader : authorizationParameter;
        if (!StringUtils.hasLength(authorization)) {
            response.setStatus(ResultCode.UNAUTHORIZED.getCode());
            servletResponse.getWriter().print(CommonUtils.gson().toJson(CommonResult.unauthorized()));
            return;
        }
        TokenVo<UserInfoVo> tokenVo = TokenUtils.unsigned(authorization, UserInfoVo.class);
        UserInfoVo userInfoVo = tokenVo.getData();
        if (userInfoVo == null) {
            response.setStatus(ResultCode.UNAUTHORIZED.getCode());
            servletResponse.getWriter().print(CommonUtils.gson().toJson(CommonResult.unauthorized()));
            return;
        }
        UserContext.set(userInfoVo.getId(), userInfoVo.getUsername(), userInfoVo);
        filterChain.doFilter(request, response);
    }
}