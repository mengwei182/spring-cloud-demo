package org.example.system.dubbo;

import org.example.system.entity.User;
import org.example.system.entity.vo.UserVO;

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
    UserVO getUserInformation(Long userId);

    /**
     * 根据username获取用户信息
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 清理用户信息缓存
     *
     * @param userId
     */
    void clearUserCache(Long userId);
}