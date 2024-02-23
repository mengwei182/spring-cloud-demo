package org.example.system.dubbo;

import org.example.system.entity.User;

/**
 * @author lihui
 * @since 2022/11/11
 */
public interface UserDubboService {
    /**
     * 根据user id获取用户信息
     *
     * @param userId
     * @return
     */
    User getUser(String userId);

    /**
     * 根据username获取用户信息
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 获取用户token有效时间
     *
     * @param userId
     * @return 过期剩余时间，单位毫秒
     */
    long getTokenExpireTime(String userId);

    /**
     * 清理用户信息缓存
     *
     * @param userId
     */
    void clearUserCache(String userId);
}