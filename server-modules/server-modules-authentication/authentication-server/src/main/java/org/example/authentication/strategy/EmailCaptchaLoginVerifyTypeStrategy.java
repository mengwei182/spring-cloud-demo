package org.example.authentication.strategy;

import cn.hutool.core.util.StrUtil;
import org.example.CaffeineRedisCache;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.SystemException;
import org.example.common.core.result.CommonServerResult;
import org.example.common.core.result.SystemServerResult;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Service
public class EmailCaptchaLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    public EmailCaptchaLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.EMAIL_CAPTCHA.getType());
    }

    @Override
    public void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        String emailCaptcha = userLoginVO.getEmailCaptcha();
        if (StrUtil.isEmpty(emailCaptcha)) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        String emailCaptchaCache = caffeineRedisCache.get(CommonServerResult.LOGIN + user.getEmail(), String.class);
        if (StrUtil.isEmpty(emailCaptchaCache)) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_OVERDUE);
        }
        if (!emailCaptcha.equalsIgnoreCase(emailCaptchaCache)) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        caffeineRedisCache.evict(CommonServerResult.LOGIN + user.getEmail());
    }
}