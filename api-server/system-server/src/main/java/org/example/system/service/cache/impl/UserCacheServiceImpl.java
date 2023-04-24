package org.example.system.service.cache.impl;

import org.example.system.service.cache.UserCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {
    private static final String PHONE_VERIFY_PREFIX = "PHONE_VERIFY_PREFIX_";
    private static final String IMAGE_VERIFY_PREFIX = "IMAGE_VERIFY_PREFIX_";
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void setPhoneVerifyCode(String phone, String verifyCode, Long timeout) {
        if (timeout != null && timeout > 0) {
            redisTemplate.opsForValue().set(PHONE_VERIFY_PREFIX.concat(phone), verifyCode, timeout, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(PHONE_VERIFY_PREFIX.concat(phone), verifyCode);
        }
    }

    @Override
    public String getPhoneVerifyCode(String phone) {
        return (String) redisTemplate.opsForValue().get(PHONE_VERIFY_PREFIX.concat(phone));
    }

    @Override
    public void deletePhoneVerifyCode(String phone) {
        redisTemplate.delete(PHONE_VERIFY_PREFIX.concat(phone));
    }

    @Override
    public void setImageVerifyCode(String account, String verifyCode, Long timeout) {
        if (timeout != null && timeout > 0) {
            redisTemplate.opsForValue().set(IMAGE_VERIFY_PREFIX.concat(account), verifyCode, timeout, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(IMAGE_VERIFY_PREFIX.concat(account), verifyCode);
        }
    }

    @Override
    public String getImageVerifyCode(String account) {
        return (String) redisTemplate.opsForValue().get(IMAGE_VERIFY_PREFIX.concat(account));
    }

    @Override
    public void deleteImageVerifyCode(String account) {
        redisTemplate.delete(IMAGE_VERIFY_PREFIX.concat(account));
    }
}