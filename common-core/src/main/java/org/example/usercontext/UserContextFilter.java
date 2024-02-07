package org.example.usercontext;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.common.result.CommonServerResult;
import org.example.common.util.TokenUtils;
import org.example.system.entity.vo.UserVO;
import org.example.usercontext.UserContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户上下文过滤器
 *
 * @author lihui
 * @since 2022/10/26
 */
@Slf4j
@Order
@WebFilter
@Component
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String authorizationHeader = request.getHeader(CommonServerResult.AUTHORIZATION);
        String authorizationParameter = request.getParameter(CommonServerResult.AUTHORIZATION);
        String authorization = !StrUtil.isEmpty(authorizationHeader) ? authorizationHeader : authorizationParameter;
        // 校验是否是不需要验证token的url
        if (StrUtil.isNotEmpty(authorization)) {
            try {
                UserContext.set(TokenUtils.unsigned(authorization, UserVO.class).getData());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}