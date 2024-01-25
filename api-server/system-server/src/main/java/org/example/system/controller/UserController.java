package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.system.vo.UserVo;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.model.CommonResult;
import org.example.common.usercontext.UserContext;
import org.example.system.api.UserQueryPage;
import org.example.system.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

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
     * @param userVo
     * @return
     */
    @RequestMapping("/add")
    public CommonResult<String> register(@Valid @RequestBody UserVo userVo) {
        return CommonResult.success(userService.addUser(userVo));
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @RequestMapping("/list")
    public CommonResult<Page<UserVo>> getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @RequestMapping("/info")
    public CommonResult<UserVo> getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getId()));
    }

    /**
     * 更新用户信息
     *
     * @param userVo
     * @return
     */
    @RequestMapping("/update")
    public CommonResult<Boolean> updateUser(@RequestBody UserVo userVo) {
        return CommonResult.success(userService.updateUser(userVo));
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

    /**
     * 创建密钥
     *
     * @param id
     * @return
     */
    @RequestMapping("/createPublicKey")
    public CommonResult<String> createPublicKey(@RequestParam String id) throws NoSuchAlgorithmException {
        return CommonResult.success(userService.createPublicKey(id));
    }
}