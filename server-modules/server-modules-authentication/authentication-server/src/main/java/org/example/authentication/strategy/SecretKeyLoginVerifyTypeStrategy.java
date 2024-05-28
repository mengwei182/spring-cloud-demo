package org.example.authentication.strategy;

import cn.hutool.core.util.StrUtil;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.SystemException;
import org.example.common.core.result.SystemServerResult;
import org.example.common.core.util.RSAEncryptUtils;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    public void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        String password = userLoginVO.getPassword();
        password = RSAEncryptUtils.decrypt(password, user.getPublicKey());
        if (StrUtil.isEmpty(password)) {
            throw new SystemException(SystemServerResult.PASSWORD_ERROR);
        }
    }
}