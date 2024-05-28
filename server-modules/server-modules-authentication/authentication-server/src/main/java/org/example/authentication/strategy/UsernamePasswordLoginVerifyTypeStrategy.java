package org.example.authentication.strategy;

import org.example.common.core.enums.UserVerifyTypeStatusEnum;
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
public class UsernamePasswordLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    @Resource
    private PasswordEncoder passwordEncoder;

    public UsernamePasswordLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.USERNAME_PASSWORD.getType());
    }

    @Override
    public void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        String password = userLoginVO.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SystemException(SystemServerResult.PASSWORD_ERROR);
        }
    }
}