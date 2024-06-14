package org.example.authentication.strategy;

import org.example.authentication.exception.AuthenticationException;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.ExceptionInformation;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Service
public class UsernamePasswordLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    @Resource
    private PasswordEncoder passwordEncoder;

    public UsernamePasswordLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.USERNAME_PASSWORD.getType());
    }

    @Override
    public void strategy(UserLoginVO userLoginVO, Object... objects) {
        User user = (User) objects[0];
        String password = userLoginVO.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2012.getCode(), ExceptionInformation.AUTHENTICATION_2012.getMessage());
        }
    }
}