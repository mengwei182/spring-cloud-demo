package org.example.authentication.service.impl;

import cn.hutool.core.util.StrUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.CaffeineRedisCache;
import org.example.authentication.entity.vo.TokenVO;
import org.example.authentication.service.TokenService;
import org.example.common.core.domain.LoginUser;
import org.example.common.core.domain.Token;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.exception.ServerException;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.TokenUtils;
import org.example.system.constant.SystemServerConstant;
import org.example.system.dubbo.TokenDubboService;
import org.example.system.dubbo.UserDubboService;
import org.example.system.entity.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@DubboService(interfaceClass = TokenDubboService.class)
public class TokenServiceImpl implements TokenService, TokenDubboService {
    @DubboReference
    private UserDubboService userDubboService;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;
    /**
     * 授权令牌accessToken的有效期，默认7天
     */
    @Value("${authentication.accessTokenExpire:7}")
    private int accessTokenExpire;
    /**
     * 刷新令牌refreshToken的有效期，默认14天
     */
    @Value("${authentication.refreshTokenExpire:14}")
    private int refreshTokenExpire;

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @Override
    public TokenVO refresh(String refreshToken) {
        if (StrUtil.isEmpty(refreshToken)) {
            throw new ServerException(ExceptionInformation.AUTHENTICATION_2005.getCode(), ExceptionInformation.AUTHENTICATION_2005.getMessage());
        }
        Token<?> unsigned;
        try {
            unsigned = TokenUtils.unsigned(refreshToken);
        } catch (Exception e) {
            throw new ServerException(ExceptionInformation.AUTHENTICATION_2006.getCode(), ExceptionInformation.AUTHENTICATION_2006.getMessage());
        }
        // token是否过期
        Date currentDate = new Date();
        Date expirationDate = unsigned.getExpirationDate();
        // refreshToken也已经过期
        if (expirationDate.getTime() < currentDate.getTime()) {
            throw new ServerException(ExceptionInformation.AUTHENTICATION_2003.getCode(), ExceptionInformation.AUTHENTICATION_2003.getMessage());
        }
        Long userId = (Long) unsigned.getId();
        // 查询并设置登录用户的resource数据
        UserVO userVO = userDubboService.getUserInformation(userId);
        if (userVO == null) {
            throw new ServerException(ExceptionInformation.AUTHENTICATION_2011.getCode(), ExceptionInformation.AUTHENTICATION_2011.getMessage());
        }
        LoginUser loginUser = CommonUtils.transformObject(userVO, LoginUser.class);
        // 登录时间
        LocalDateTime localDateTime = LocalDateTime.now();
        Date loginDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 生成accessToken
        Token<LoginUser> accessTokenEntity = new Token<>(unsigned.getId(), loginDate, Date.from(localDateTime.plusDays(accessTokenExpire).atZone(ZoneId.systemDefault()).toInstant()), loginUser);
        String accessToken = TokenUtils.sign(accessTokenEntity);
        // 生成refreshToken
        Token<LoginUser> refreshTokenEntity = new Token<>(unsigned.getId(), loginDate, Date.from(localDateTime.plusDays(refreshTokenExpire).atZone(ZoneId.systemDefault()).toInstant()), loginUser);
        String refreshTokenNew = TokenUtils.sign(refreshTokenEntity);
        // 删除旧缓存
        caffeineRedisCache.evict(userId);
        caffeineRedisCache.evict(SystemServerConstant.USER_TOKEN_KEY + userId);
        // 设置新缓存
        caffeineRedisCache.put(userId, loginUser, Duration.ofDays(accessTokenExpire));
        caffeineRedisCache.put(SystemServerConstant.USER_TOKEN_KEY + userId, accessToken, Duration.ofDays(accessTokenExpire));
        return new TokenVO(accessToken, refreshTokenNew);
    }

    /**
     * 清理用户token缓存
     *
     * @param userId
     */
    @Override
    public void clearTokenCache(Long userId) {
        caffeineRedisCache.evict(SystemServerConstant.USER_TOKEN_KEY + userId);
    }
}