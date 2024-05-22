package org.example.authentication.service.impl;

import cn.hutool.core.util.StrUtil;
import org.example.CaffeineRedisCache;
import org.example.authentication.service.LoginVerifyStrategy;
import org.example.common.core.enums.UserVerifyStatusEnum;
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
public class EmailCaptchaLoginVerifyStrategy extends LoginVerifyStrategy {
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    public EmailCaptchaLoginVerifyStrategy() {
        super(UserVerifyStatusEnum.EMAIL_CAPTCHA.getStatus());
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