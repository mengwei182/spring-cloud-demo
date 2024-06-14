package org.example.authentication.strategy;

import cn.hutool.core.util.StrUtil;
import org.example.CaffeineRedisCache;
import org.example.authentication.exception.AuthenticationException;
import org.example.common.core.constant.RedisKeyConstant;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.ExceptionInformation;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author lihui
 * @since 2024/5/22
 */
@Service
public class ImageCaptchaLoginVerifyTypeStrategy extends LoginVerifyTypeStrategy {
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    public ImageCaptchaLoginVerifyTypeStrategy() {
        super(UserVerifyTypeStatusEnum.IMAGE_CAPTCHA.getType());
    }

    @Override
    public void strategy(UserLoginVO userLoginVO, Object... objects) {
        String imageCaptcha = userLoginVO.getImageCaptcha();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new AuthenticationException(ExceptionInformation.EXCEPTION_1000.getCode(), ExceptionInformation.EXCEPTION_1000.getMessage());
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession(false);
        if (StrUtil.isEmpty(imageCaptcha) || session == null) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        String imageCaptchaCache = caffeineRedisCache.get(RedisKeyConstant.LOGIN_IMAGE_CAPTCHA + session.getId(), String.class);
        if (StrUtil.isEmpty(imageCaptchaCache)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        if (!imageCaptcha.equalsIgnoreCase(imageCaptchaCache)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2014.getCode(), ExceptionInformation.AUTHENTICATION_2014.getMessage());
        }
        caffeineRedisCache.evict(RedisKeyConstant.LOGIN_IMAGE_CAPTCHA + session.getId());
    }
}