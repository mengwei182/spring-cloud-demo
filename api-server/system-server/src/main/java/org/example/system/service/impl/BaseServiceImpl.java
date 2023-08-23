package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.User;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.error.SystemServerResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.ImageCaptchaUtils;
import org.example.common.util.RSAEncryptUtils;
import org.example.common.util.TokenUtils;
import org.example.system.mapper.UserMapper;
import org.example.system.service.BaseService;
import org.example.system.service.cache.ResourceCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ResourceCacheService resourceCacheService;
    @Value("${key.encrypt.enable}")
    private Boolean keyEncryptEnable;

    /**
     * 登录
     *
     * @param request
     * @param usernamePasswordVo
     * @return
     */
    @Override
    public String login(HttpServletRequest request, UsernamePasswordVo usernamePasswordVo) throws Exception {
        String username = usernamePasswordVo.getUsername();
        String password = usernamePasswordVo.getPassword();
        String captcha = usernamePasswordVo.getCaptcha();
        if (!StringUtils.hasLength(username)) {
            throw new CommonException(SystemServerResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(SystemServerResult.PASSWORD_NULL);
        }
        if (!StringUtils.hasLength(captcha)) {
            throw new CommonException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        HttpSession session = request.getSession(false);
        String captchaMemory = redisTemplate.opsForValue().get(session.getId());
        if (!StringUtils.hasLength(captchaMemory)) {
            throw new CommonException(SystemServerResult.VERIFY_CODE_OVERDUE);
        }
        if (!captcha.equalsIgnoreCase(captchaMemory)) {
            throw new CommonException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new CommonException(SystemServerResult.USER_NOT_EXIST);
        }
        // 开启使用公私钥后使用私钥解密
        if (Boolean.TRUE.equals(keyEncryptEnable)) {
            password = RSAEncryptUtils.decrypt(password);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CommonException(SystemServerResult.PASSWORD_ERROR);
        }
        UserInfoVo userInfoVo = CommonUtils.transformObject(user, UserInfoVo.class);
        // 查询并设置登录用户的resource数据
        userInfoVo.setResources(resourceCacheService.getResourceByUserId(user.getId()));
        Date loginTime = new Date();
        TokenVo<?> tokenVo = new TokenVo<>(user.getId(), loginTime, userInfoVo);
        String token = TokenUtils.sign(tokenVo);
        // 设置token到redis，有效期一个小时
        redisTemplate.opsForValue().set(user.getId(), token, 60, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 登出
     *
     * @return
     */
    @Override
    public Boolean logout() {
        UserInfoVo userInfoVo = UserContext.get().getUserInfoVo();
        if (userInfoVo == null) {
            return true;
        }
        redisTemplate.delete(userInfoVo.getId());
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
        redisTemplate.opsForValue().set(session.getId(), captcha, 10L, TimeUnit.MINUTES);
        os.close();
    }
}