package org.example.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.example.CaffeineRedisCache;
import org.example.common.entity.base.Token;
import org.example.common.entity.system.User;
import org.example.common.entity.system.vo.UserVo;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.enums.UserVerifyStatusEnum;
import org.example.common.result.CommonServerResult;
import org.example.common.result.SystemServerResult;
import org.example.common.result.exception.SystemException;
import org.example.common.usercontext.UserContext;
import org.example.system.mapper.UserMapper;
import org.example.system.service.BaseService;
import org.example.system.service.ResourceService;
import org.example.system.service.TokenService;
import org.example.system.service.UserService;
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
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;
    @Resource
    private ResourceService resourceService;
    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    /**
     * 登录
     *
     * @param request
     * @param usernamePasswordVo
     * @return
     */
    @Override
    public String login(HttpServletRequest request, UsernamePasswordVo usernamePasswordVo) {
        String username = usernamePasswordVo.getUsername();
        String password = usernamePasswordVo.getPassword();
        if (StrUtil.isEmpty(username)) {
            throw new SystemException(SystemServerResult.USERNAME_NULL);
        }
        if (StrUtil.isEmpty(password)) {
            throw new SystemException(SystemServerResult.PASSWORD_NULL);
        }
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
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
                String imageCaptcha = usernamePasswordVo.getImageCaptcha();
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
                String phoneCaptcha = usernamePasswordVo.getPhoneCaptcha();
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
                String emailCaptcha = usernamePasswordVo.getEmailCaptcha();
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
        long time = userService.getTokenExpireTime(user.getId());
        // token已经过期
        if (time <= 0) {
            // 删除旧token
            userService.clear(user.getId());
            throw new SystemException(SystemServerResult.TOKEN_EXPIRATION_TIME_INVALID);
        }
        UserVo userVo = CommonUtils.transformObject(user, UserVo.class);
        // 查询并设置登录用户的resource数据
        userVo.setResources(resourceService.getResourceByUserId(user.getId()));
        Token<UserVo> token = new Token<>(user.getId(), new Date(), userVo);
        String tokenString = TokenUtils.sign(token);
        userService.clear(user.getId());
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
        UserVo userVo = UserContext.get();
        if (userVo == null) {
            return true;
        }
        userService.clear(userVo.getId());
        tokenService.clear(userVo.getId());
        resourceService.clear(userVo.getId());
        clear(userVo.getId());
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

    @Override
    public void clear(Object... ids) {
        caffeineRedisCache.evict(SystemServerResult.USER_TOKEN_KEY + ids[0]);
    }
}