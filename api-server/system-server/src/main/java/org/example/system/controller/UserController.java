package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "用户管理")
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
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public CommonResult<String> register(@Valid @RequestBody UserVo userVo) {
        return CommonResult.success(userService.addUser(userVo));
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @ApiOperation("分页查看用户列表")
    @GetMapping("/list")
    public CommonResult<Page<UserVo>> getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @ApiOperation("查看用户详情")
    @GetMapping("/info")
    public CommonResult<UserVo> getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getId()));
    }

    /**
     * 更新用户信息
     *
     * @param userVo
     * @return
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUser(@RequestBody UserVo userVo) {
        return CommonResult.success(userService.updateUser(userVo));
    }

    /**
     * 更新密码
     *
     * @param usernamePasswordVo
     * @return
     */
    @ApiOperation("更新密码")
    @PostMapping("/updatePassword")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(userService.updateUserPassword(usernamePasswordVo));
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteUser(@RequestParam String id) {
        return CommonResult.success(userService.deleteUser(id));
    }

    /**
     * 获取密钥
     *
     * @param id
     * @return
     */
    @ApiOperation("获取密钥")
    @GetMapping("/getPublicKey")
    public CommonResult<String> getPublicKey(@RequestParam String id) throws NoSuchAlgorithmException {
        return CommonResult.success(userService.getPublicKey(id));
    }
}