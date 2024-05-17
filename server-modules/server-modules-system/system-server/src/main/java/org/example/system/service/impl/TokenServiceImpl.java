package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.CaffeineRedisCache;
import org.example.common.core.entity.Token;
import org.example.common.core.result.SystemServerResult;
import org.example.common.core.result.exception.SystemException;
import org.example.common.core.util.TokenUtils;
import org.example.system.dubbo.TokenDubboService;
import org.example.system.entity.vo.UserVO;
import org.example.system.service.TokenService;
import org.example.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = TokenDubboService.class)
public class TokenServiceImpl implements TokenService, TokenDubboService {
    @Resource
    private UserService userService;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    /**
     * 刷新token
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public String refresh(String userId) {
        UserVO userVO = userService.getUserInfo(userId);
        if (userVO == null) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        long time = userService.getTokenExpireTime(userId);
        // token已经过期
        if (time <= 0) {
            // 删除token缓存
            clearTokenCache(userId);
            throw new SystemException(SystemServerResult.TOKEN_EXPIRATION_TIME_INVALID);
        }
        Token<UserVO> token = new Token<>(userId, new Date(), userVO);
        // 重新设置token
        caffeineRedisCache.put(SystemServerResult.USER_TOKEN_KEY + userId, token, Duration.ofMillis(time));
        return TokenUtils.sign(token);
    }

    /**
     * 清理用户token缓存
     *
     * @param userId
     */
    @Override
    public void clearTokenCache(String userId) {
        caffeineRedisCache.evict(SystemServerResult.USER_TOKEN_KEY + userId);
    }
}