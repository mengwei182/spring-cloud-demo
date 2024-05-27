package org.example.authentication.service.impl;

import cn.hutool.core.util.StrUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.CaffeineRedisCache;
import org.example.authentication.service.TokenService;
import org.example.common.core.domain.LoginUser;
import org.example.common.core.domain.Token;
import org.example.common.core.exception.AuthenticationException;
import org.example.common.core.exception.SystemException;
import org.example.common.core.result.AuthenticationResult;
import org.example.common.core.result.SystemServerResult;
import org.example.common.core.usercontext.UserContext;
import org.example.common.core.util.TokenUtils;
import org.example.system.dubbo.TokenDubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = TokenDubboService.class)
public class TokenServiceImpl implements TokenService, TokenDubboService {
    @Resource
    private CaffeineRedisCache caffeineRedisCache;

    /**
     * 刷新token
     *
     * @return
     */
    @Override
    public String refresh() {
        LoginUser loginUser = UserContext.get();
        if (loginUser == null) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        try {
            String userId = loginUser.getId();
            String tokenString = caffeineRedisCache.get(SystemServerResult.USER_TOKEN_KEY + userId, String.class);
            if (StrUtil.isEmpty(tokenString)) {
                throw new AuthenticationException(AuthenticationResult.TOKEN_VALID);
            }
            Token<?> oldToken = TokenUtils.unsigned(tokenString);
            // 删除token缓存
            clearTokenCache(userId);
            // 过期时间
            LocalDateTime expirationDateTime = LocalDateTime.ofInstant(oldToken.getExpirationDate().toInstant(), ZoneId.systemDefault()).plusDays(Token.EXPIRATION_DAY);
            Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
            // 重新签名生成token
            Token<?> token = new Token<>(userId, oldToken.getSignDate(), expirationDate, oldToken.getData());
            String refresh = TokenUtils.sign(token);
            // 重新设置token
            caffeineRedisCache.put(SystemServerResult.USER_TOKEN_KEY + userId, refresh, Duration.ofDays(Token.EXPIRATION_DAY));
            return refresh;
        } catch (Exception e) {
            throw new AuthenticationException(AuthenticationResult.TOKEN_REFRESH_FAIL);
        }
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