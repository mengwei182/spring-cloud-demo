package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.system.entity.vo.UserLoginVO;
import org.example.system.entity.vo.UserVO;
import org.example.system.entity.query.UserQueryPage;

import java.security.NoSuchAlgorithmException;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface UserService {
    /**
     * 新增用户
     *
     * @param userVO
     * @return
     */
    String addUser(UserVO userVO);

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    Page<UserVO> getUserList(UserQueryPage queryPage);

    /**
     * 查看用户详情
     *
     * @return
     */
    UserVO getUserInformation(String id);

    /**
     * 更新用户信息
     *
     * @param userVO
     * @return
     */
    Boolean updateUser(UserVO userVO);

    /**
     * 更新密码
     *
     * @param userLoginVo
     * @return
     */
    Boolean updateUserPassword(UserLoginVO userLoginVo);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    Boolean deleteUser(String id);

    /**
     * 获取密钥
     *
     * @param id
     * @return
     */
    String getPublicKey(String id) throws NoSuchAlgorithmException;
}