package org.example.authentication.strategy;

import cn.hutool.core.util.StrUtil;
import org.example.CaffeineRedisCache;
import org.example.authentication.exception.AuthenticationException;
import org.example.common.core.constant.CommonConstant;
import org.example.common.core.constant.RedisKeyConstant;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.ExceptionInformation;
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
public class PhoneCaptchaLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    public PhoneCaptchaLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.PHONE_CAPTCHA.getType());
    }

    @Override
    public void strategy(UserLoginVO userLoginVO, Object... objects) {
        User user = (User) objects[0];
        String phoneCaptcha = userLoginVO.getPhoneCaptcha();
        if (StrUtil.isEmpty(phoneCaptcha)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        String phoneCaptchaCache = caffeineRedisCache.get(RedisKeyConstant.LOGIN_PHONE_CAPTCHA + user.getPhone(), String.class);
        if (StrUtil.isEmpty(phoneCaptchaCache)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        if (!phoneCaptcha.equalsIgnoreCase(phoneCaptchaCache)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        caffeineRedisCache.evict(RedisKeyConstant.LOGIN_PHONE_CAPTCHA + user.getPhone());
    }
}