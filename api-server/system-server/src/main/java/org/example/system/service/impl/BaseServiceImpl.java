package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.TokenVo;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.User;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.error.SystemServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.TokenUtils;
import org.example.system.mapper.UserMapper;
import org.example.system.service.BaseService;
import org.example.system.service.cache.ResourceCacheService;
import org.example.system.service.cache.UserCacheService;
import org.example.system.util.ImageVerifyCodeUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    private UserCacheService userCacheService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ResourceCacheService resourceCacheService;

    /**
     * 登录
     *
     * @param usernamePasswordVo
     * @return
     */
    @Override
    public String login(UsernamePasswordVo usernamePasswordVo) {
        String username = usernamePasswordVo.getUsername();
        String password = usernamePasswordVo.getPassword();
        if (!StringUtils.hasLength(username)) {
            throw new CommonException(SystemServerErrorResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(SystemServerErrorResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new CommonException(SystemServerErrorResult.USER_NOT_EXIST);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CommonException(SystemServerErrorResult.PASSWORD_ERROR);
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
        UserContext.remove();
        return true;
    }

    /**
     * 获取图片验证码
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void generateImageVerifyCode(HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        String verifyCode = ImageVerifyCodeUtils.outputVerifyImage(130, 30, servletOutputStream, 6);
        userCacheService.setImageVerifyCode(UserContext.get().getUserId(), verifyCode, 5L);
        servletOutputStream.close();
    }
}