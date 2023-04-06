package org.example.system.controller;

import org.example.common.entity.vo.UserInfoVo;
import org.example.common.model.CommonResult;
import org.example.common.usercontext.UserContext;
import org.example.system.api.UserQueryPage;
import org.example.system.entity.vo.UserRoleRelationVo;
import org.example.system.entity.vo.UsernamePasswordVo;
import org.example.system.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param usernamePasswordVo
     * @return
     */
    @RequestMapping("/login")
    public CommonResult login(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.login(usernamePasswordVo));
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success(userService.logout());
    }

    /**
     * 注册
     *
     * @param userInfoVo
     * @return
     */
    @RequestMapping("/register")
    public CommonResult register(@Valid @RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.register(userInfoVo));
    }

    /**
     * 图片验证码
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/image/verify/code")
    public void getImageVerifyCode(HttpServletResponse response) throws IOException {
        userService.generateImageVerifyCode(response);
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @RequestMapping("/user/list")
    public CommonResult getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @RequestMapping("/user/info")
    public CommonResult getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getUserId()));
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVo
     * @return
     */
    @RequestMapping("/user/update")
    public CommonResult updateUser(@RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.updateUser(userInfoVo));
    }

    /**
     * 更新密码
     *
     * @param usernamePasswordVo
     * @return
     */
    @RequestMapping("/user/updatePassword")
    public CommonResult updateUserPassword(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.updateUserPassword(usernamePasswordVo));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("/user/delete")
    public CommonResult deleteUser(@RequestParam String id) {
        return CommonResult.success(userService.deleteUser(id));
    }
}