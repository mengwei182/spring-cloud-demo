package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.vo.UserInfoVo;
import org.example.user.api.UserQueryPage;
import org.example.user.entity.vo.UserRoleRelationVo;
import org.example.user.entity.vo.UsernamePasswordVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {
    String login(UsernamePasswordVo usernamePasswordVo);

    Boolean logout();

    Boolean register(UserInfoVo userInfoVo);

    Boolean generatePhoneVerifyCode(String phone);

    void generateImageVerifyCode(HttpServletResponse response) throws IOException;

    UserInfoVo getUserInfo(String id);

    Page<UserInfoVo> getUserList(UserQueryPage queryPage);

    Boolean updateUser(UserInfoVo userInfoVo);

    Boolean updateUserPassword(UsernamePasswordVo usernamePasswordVo);

    Boolean updateUserRole(UserRoleRelationVo userRoleRelationVo);

    Boolean deleteUser(String id);
}