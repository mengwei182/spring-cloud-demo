package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.cache.CacheService;
import org.example.common.entity.system.vo.UserVo;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.system.api.UserQueryPage;

import java.security.NoSuchAlgorithmException;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface UserService extends CacheService {
    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    String addUser(UserVo userVo);

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    Page<UserVo> getUserList(UserQueryPage queryPage);

    /**
     * 查看用户详情
     *
     * @return
     */
    UserVo getUserInfo(String id);

    /**
     * 更新用户信息
     *
     * @param userVo
     * @return
     */
    Boolean updateUser(UserVo userVo);

    /**
     * 更新密码
     *
     * @param usernamePasswordVo
     * @return
     */
    Boolean updateUserPassword(UsernamePasswordVo usernamePasswordVo);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    Boolean deleteUser(String id);

    /**
     * 获取用户token有效时间
     *
     * @param id
     * @return 有效时间，单位毫秒
     */
    long getTokenExpireTime(String id);

    /**
     * 创建密钥
     *
     * @param id
     * @return
     */
    String createPublicKey(String id) throws NoSuchAlgorithmException;
}