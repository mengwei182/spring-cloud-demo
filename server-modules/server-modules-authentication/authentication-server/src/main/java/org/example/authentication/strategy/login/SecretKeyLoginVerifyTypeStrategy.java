package org.example.authentication.strategy.login;

import cn.hutool.core.util.StrUtil;
import org.example.authentication.exception.AuthenticationException;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.util.RSAEncryptUtils;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.stereotype.Service;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Service
public class SecretKeyLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    public SecretKeyLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.SECRET_KEY.getType());
    }

    @Override
    public void strategy(UserLoginVO userLoginVO, Object... objects) {
        User user = (User) objects[0];
        String password = userLoginVO.getPassword();
        password = RSAEncryptUtils.decrypt(password, user.getPublicKey());
        if (StrUtil.isEmpty(password)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2012.getCode(), ExceptionInformation.AUTHENTICATION_2012.getMessage());
        }
    }
}