package org.example.user.controller;

import org.example.common.entity.vo.UserInfoVo;
import org.example.common.model.CommonResult;
import org.example.common.usercontext.UserContext;
import org.example.user.api.UserQueryPage;
import org.example.user.entity.vo.UserRoleRelationVo;
import org.example.user.entity.vo.UsernamePasswordVo;
import org.example.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public CommonResult login(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.login(usernamePasswordVo));
    }

    @RequestMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success(userService.logout());
    }

    @RequestMapping("/register")
    public CommonResult register(@Valid @RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.register(userInfoVo));
    }

    @RequestMapping("/phone/verify/code")
    public CommonResult getPhoneVerifyCode(@RequestParam String phone) {
        return CommonResult.success(userService.generatePhoneVerifyCode(phone));
    }

    @RequestMapping("/image/verify/code")
    public void getImageVerifyCode(HttpServletResponse response) throws IOException {
        userService.generateImageVerifyCode(response);
    }

    @RequestMapping("/user/list")
    public CommonResult getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    @RequestMapping("/user/info")
    public CommonResult getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getUserId()));
    }

    @RequestMapping("/user/edit")
    public CommonResult updateUser(@RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.updateUser(userInfoVo));
    }

    @RequestMapping("/user/updatePassword")
    public CommonResult updateUserPassword(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.updateUserPassword(usernamePasswordVo));
    }

    @RequestMapping("/user/updateRole")
    public CommonResult updateUserRole(@RequestBody UserRoleRelationVo userRoleRelationVo) {
        return CommonResult.success(userService.updateUserRole(userRoleRelationVo));
    }

    @RequestMapping("/user/delete")
    public CommonResult deleteUser(@RequestParam String id) {
        return CommonResult.success(userService.deleteUser(id));
    }
}