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
    public void strategy(HttpServletRequest request, UserLoginVO userLoginVO, User user) {
        String imageCaptcha = userLoginVO.getImageCaptcha();
        HttpSession session = request.getSession(false);
        if (StrUtil.isEmpty(imageCaptcha) || session == null) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        String imageCaptchaCache = caffeineRedisCache.get(CommonServerResult.LOGIN + session.getId(), String.class);
        if (StrUtil.isEmpty(imageCaptchaCache)) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_OVERDUE);
        }
        if (!imageCaptcha.equalsIgnoreCase(imageCaptchaCache)) {
            throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        caffeineRedisCache.evict(CommonServerResult.LOGIN + session.getId());
    }
}