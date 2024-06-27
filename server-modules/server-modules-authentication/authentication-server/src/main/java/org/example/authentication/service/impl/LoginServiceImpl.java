package org.example.authentication.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.CaffeineRedisCache;
import org.example.authentication.exception.AuthenticationException;
import org.example.authentication.service.LoginService;
import org.example.authentication.strategy.LoginVerifyTypeStrategy;
import org.example.common.core.domain.LoginUser;
import org.example.common.core.domain.Token;
import org.example.common.core.enums.UserVerifyTypeStatusEnum;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.usercontext.UserContext;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.ImageCaptchaUtils;
import org.example.common.core.util.TokenUtils;
import org.example.system.constant.SystemServerConstant;
import org.example.system.dubbo.TokenDubboService;
import org.example.system.dubbo.UserDubboService;
import org.example.system.entity.User;
import org.example.system.entity.vo.UserLoginVO;
import org.example.system.entity.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @DubboReference
    private UserDubboService userDubboService;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;
    @DubboReference
    private TokenDubboService tokenDubboService;

    /**
     * 登录
     *
     * @param userLoginVO
     * @return
     */
    @Override
    public String login(UserLoginVO userLoginVO) {
        String username = userLoginVO.getUsername();
        String password = userLoginVO.getPassword();
        if (StrUtil.isEmpty(username)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2007.getCode(), ExceptionInformation.AUTHENTICATION_2007.getMessage());
        }
        if (StrUtil.isEmpty(password)) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2008.getCode(), ExceptionInformation.AUTHENTICATION_2008.getMessage());
        }
        User user = userDubboService.getUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2011.getCode(), ExceptionInformation.AUTHENTICATION_2011.getMessage());
        }
        String verifyStatusString = user.getVerifyStatus();
        // 没有指定登录验证类型，默认为账号密码验证类型
        if (StrUtil.isEmpty(verifyStatusString)) {
            verifyStatusString = String.valueOf(UserVerifyTypeStatusEnum.USERNAME_PASSWORD.getType());
        }
        // 登录验证流程
        String[] verifyStatusSplit = verifyStatusString.split(",");
        for (String vs : verifyStatusSplit) {
            int verifyStatus;
            try {
                verifyStatus = Integer.parseInt(vs);
            } catch (Exception e) {
                throw new AuthenticationException(ExceptionInformation.AUTHENTICATION_2009.getCode(), ExceptionInformation.AUTHENTICATION_2009.getMessage());
            }
            LoginVerifyTypeStrategy.verify(verifyStatus, userLoginVO, user);
        }
        LoginUser loginUser = CommonUtils.transformObject(user, LoginUser.class);
        // 查询并设置登录用户的resource数据
        UserVO userVO = userDubboService.getUserInformation(user.getId());
        loginUser.setResourceUrls(userVO.getResourceUrls());
        // 登录时间
        LocalDateTime localDateTime = LocalDateTime.now();
        Date loginDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 过期时间
        LocalDateTime expirationDateTime = localDateTime.plusDays(Token.EXPIRATION_DAY);
        Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 生成token
        Token<LoginUser> token = new Token<>(user.getId(), loginDate, expirationDate, loginUser);
        String tokenString = TokenUtils.sign(token);
        // 删除旧缓存
        caffeineRedisCache.evict(user.getId());
        caffeineRedisCache.evict(SystemServerConstant.USER_TOKEN_KEY + user.getId());
        // 设置新缓存
        caffeineRedisCache.put(user.getId(), loginUser, Duration.ofDays(Token.EXPIRATION_DAY));
        caffeineRedisCache.put(SystemServerConstant.USER_TOKEN_KEY + user.getId(), tokenString, Duration.ofDays(Token.EXPIRATION_DAY));
        return tokenString;
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public Boolean logout() {
        LoginUser loginUser = UserContext.get();
        if (loginUser == null) {
            return true;
        }
        userDubboService.clearUserCache(loginUser.getId());
        tokenDubboService.clearTokenCache(loginUser.getId());
        caffeineRedisCache.evict(SystemServerConstant.USER_TOKEN_KEY + loginUser.getId());
        UserContext.remove();
        return true;
    }

    /**
     * 生成图片验证码
     *
     * @param width 图片宽度
     * @param height 图片高度
     * @param captchaSize 验证码位数
     * @throws IOException
     */
    @Override
    public void generateImageCaptcha(Integer width, Integer height, Integer captchaSize) throws IOException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new AuthenticationException(ExceptionInformation.EXCEPTION_1000.getCode(), ExceptionInformation.EXCEPTION_1000.getMessage());
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        if (response == null) {
            throw new AuthenticationException(ExceptionInformation.EXCEPTION_1000.getCode(), ExceptionInformation.EXCEPTION_1000.getMessage());
        }
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