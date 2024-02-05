package org.example.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.example.CaffeineRedisCache;
import org.example.common.entity.Token;
import org.example.common.enums.UserVerifyStatusEnum;
import org.example.common.result.CommonServerResult;
import org.example.common.result.SystemServerResult;
import org.example.common.result.exception.SystemException;
import org.example.system.dubbo.ResourceDubboService;
import org.example.system.dubbo.TokenDubboService;
import org.example.system.dubbo.UserDubboService;
import org.example.system.entity.User;
import org.example.usercontext.UserContext;
import org.example.system.service.BaseService;
import org.example.system.vo.UserLoginVO;
import org.example.system.vo.UserVO;
import org.example.util.CommonUtils;
import org.example.util.ImageCaptchaUtils;
import org.example.util.RSAEncryptUtils;
import org.example.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
@Service
public class BaseServiceImpl implements BaseService {
    @Resource
    private UserDubboService userDubboService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;
    @Resource
    private ResourceDubboService resourceDubboService;
    @Resource
    private TokenDubboService tokenDubboService;

    /**
     * 登录
     *
     * @param request
     * @param userLoginVO
     * @return
     */
    @Override
    public String login(HttpServletRequest request, UserLoginVO userLoginVO) {
        String username = userLoginVO.getUsername();
        String password = userLoginVO.getPassword();
        if (StrUtil.isEmpty(username)) {
            throw new SystemException(SystemServerResult.USERNAME_NULL);
        }
        if (StrUtil.isEmpty(password)) {
            throw new SystemException(SystemServerResult.PASSWORD_NULL);
        }
        User user = userDubboService.getUserByUsername(username);
        if (user == null) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        String verifyStatusString = user.getVerifyStatus();
        if (StrUtil.isEmpty(verifyStatusString)) {
            verifyStatusString = String.valueOf(UserVerifyStatusEnum.NULL.getStatus());
        }
        // 登录验证流程，可以改写为策略模式
        String[] verifyStatusSplit = verifyStatusString.split(",");
        for (String vs : verifyStatusSplit) {
            int verifyStatus = Integer.parseInt(vs);
            if (verifyStatus == UserVerifyStatusEnum.NULL.getStatus()) {
                break;
            }
            // 图片验证码
            if (verifyStatus == UserVerifyStatusEnum.IMAGE.getStatus()) {
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
            // 短信验证码
            if (verifyStatus == UserVerifyStatusEnum.PHONE.getStatus()) {
                String phoneCaptcha = userLoginVO.getPhoneCaptcha();
                if (StrUtil.isEmpty(phoneCaptcha)) {
                    throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
                }
                String phoneCaptchaCache = caffeineRedisCache.get(CommonServerResult.LOGIN + user.getPhone(), String.class);
                if (StrUtil.isEmpty(phoneCaptchaCache)) {
                    throw new SystemException(SystemServerResult.VERIFY_CODE_OVERDUE);
                }
                if (!phoneCaptcha.equalsIgnoreCase(phoneCaptchaCache)) {
                    throw new SystemException(SystemServerResult.VERIFY_CODE_ERROR);
                }
                caffeineRedisCache.evict(CommonServerResult.LOGIN + user.getPhone());
            }
            // 开启使用公私钥后使用私钥解密
            if (verifyStatus == UserVerifyStatusEnum.SECRET_KEY.getStatus()) {
                password = RSAEncryptUtils.decrypt(password, user.getPublicKey());
                if (StrUtil.isEmpty(password)) {
                    throw new SystemException(SystemServerResult.PASSWORD_ERROR);
                }
            }
            // 邮箱验证码
            if (verifyStatus == UserVerifyStatusEnum.EMAIL.getStatus()) {
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
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SystemException(SystemServerResult.PASSWORD_ERROR);
        }
        long time = userDubboService.getTokenExpireTime(user.getId());
        // token已经过期
        if (time <= 0) {
            // 删除旧token
            userDubboService.clearUserCache(user.getId());
            throw new SystemException(SystemServerResult.TOKEN_EXPIRATION_TIME_INVALID);
        }
        UserVO userVO = CommonUtils.transformObject(user, UserVO.class);
        // 查询并设置登录用户的resource数据
        userVO.setResources(resourceDubboService.getResourceByUserId(user.getId()));
        Token<UserVO> token = new Token<>(user.getId(), new Date(), userVO);
        String tokenString = TokenUtils.sign(token);
        userDubboService.clearUserCache(user.getId());
        caffeineRedisCache.put(SystemServerResult.USER_TOKEN_KEY + user.getId(), tokenString, Duration.ofMillis(time));
        return tokenString;
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public Boolean logout() {
        UserVO userVO = UserContext.get();
        if (userVO == null) {
            return true;
        }
        userDubboService.clearUserCache(userVO.getId());
        tokenDubboService.clearTokenCache(userVO.getId());
        resourceDubboService.clearResourceCache(userVO.getId());
        caffeineRedisCache.evict(SystemServerResult.USER_TOKEN_KEY + userVO.getId());
        UserContext.remove();
        return true;
    }

    /**
     * 生成图片验证码
     *
     * @param request
     * @param response
     * @param width 图片宽度
     * @param height 图片高度
     * @param captchaSize 验证码位数
     * @throws IOException
     */
    @Override
    public void generateImageCaptcha(HttpServletRequest request, HttpServletResponse response, Integer width, Integer height, Integer captchaSize) throws IOException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream os = response.getOutputStream();
        HttpSession session = request.getSession(true);
        String captcha = ImageCaptchaUtils.outputCaptchaImage(width, height, os, captchaSize);
        caffeineRedisCache.put(session.getId(), captcha, Duration.ofMinutes(5));
        os.close();
    }
}