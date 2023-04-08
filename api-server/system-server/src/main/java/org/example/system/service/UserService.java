package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.system.api.UserQueryPage;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface UserService {
    /**
     * 新增用户
     *
     * @param userInfoVo
     * @return
     */
    Boolean addUser(UserInfoVo userInfoVo);

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    Page<UserInfoVo> getUserList(UserQueryPage queryPage);

    /**
     * 查看用户详情
     *
     * @return
     */
    UserInfoVo getUserInfo(String id);

    /**
     * 更新用户信息
     *
     * @param userInfoVo
     * @return
     */
    Boolean updateUser(UserInfoVo userInfoVo);

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
}