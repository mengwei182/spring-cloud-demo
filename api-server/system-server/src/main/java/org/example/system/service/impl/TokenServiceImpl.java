package org.example.system.service.impl;

import org.example.common.entity.base.Token;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.error.SystemServerResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.TokenUtils;
import org.example.system.service.TokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 刷新token
     *
     * @param expiration 过期时间
     * @return
     */
    @Override
    public String refresh(Long expiration) {
        Date date = new Date();
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTime(date);
        minCalendar.add(Calendar.DATE, 1);
        long minTime = minCalendar.getTime().getTime();
        if (expiration < minTime) {
            throw new CommonException(SystemServerResult.TOKEN_EXPIRATION_TIMEOUT_MIN);
        }
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.setTime(date);
        maxCalendar.add(Calendar.YEAR, 3);
        long maxExpiration = maxCalendar.getTime().getTime();
        if (expiration > maxExpiration) {
            throw new CommonException(SystemServerResult.TOKEN_EXPIRATION_TIMEOUT_MAX);
        }
        UserInfoVo userInfoVo = UserContext.get();
        String userId = userInfoVo.getId();
        // 删除已存储的用户token
        redisTemplate.delete(userId);
        userInfoVo.setLoginTime(date);
        Token<?> token = new Token<>(userId, date, userInfoVo);
        // 重新设置token
        redisTemplate.opsForValue().set(userId, token, 60 * 60, TimeUnit.SECONDS);
        return TokenUtils.sign(token);
    }
}