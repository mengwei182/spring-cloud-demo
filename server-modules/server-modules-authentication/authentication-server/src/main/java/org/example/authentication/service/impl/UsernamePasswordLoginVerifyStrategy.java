package org.example.authentication.service.impl;

import org.example.authentication.service.LoginVerifyStrategy;
import org.example.common.core.enums.UserVerifyStatusEnum;
import org.example.common.core.exception.SystemException;
import org.example.common.core.result.SystemServerResult;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Service
public class UsernamePasswordLoginVerifyStrategy extends LoginVerifyStrategy {
    @Resource
    private PasswordEncoder passwordEncoder;

    public UsernamePasswordLoginVerifyStrategy() {
        super(UserVerifyStatusEnum.USERNAME_PASSWORD.getStatus());
    }

    @Override
    public void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        String password = userLoginVO.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SystemException(SystemServerResult.PASSWORD_ERROR);
        }
    }
}