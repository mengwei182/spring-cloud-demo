package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.model.CommonResult;
import org.example.common.usercontext.UserContext;
import org.example.system.api.UserQueryPage;
import org.example.system.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lihui
 * @since 2023/4/3
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 新增用户
     *
     * @param userInfoVo
     * @return
     */
    @RequestMapping("/add")
    public CommonResult<Boolean> register(@Valid @RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.addUser(userInfoVo));
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @RequestMapping("/list")
    public CommonResult<Page<UserInfoVo>> getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @RequestMapping("/info")
    public CommonResult<UserInfoVo> getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getId()));
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVo
     * @return
     */
    @RequestMapping("/update")
    public CommonResult<Boolean> updateUser(@RequestBody UserInfoVo userInfoVo) {
        return CommonResult.success(userService.updateUser(userInfoVo));
    }

    /**
     * 更新密码
     *
     * @param usernamePasswordVo
     * @return
     */
    @RequestMapping("/updatePassword")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.updateUserPassword(usernamePasswordVo));
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public CommonResult<Boolean> deleteUser(@RequestParam String id) {
        return CommonResult.success(userService.deleteUser(id));
    }
}