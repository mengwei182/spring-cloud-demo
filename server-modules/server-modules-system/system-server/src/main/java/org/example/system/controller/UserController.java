package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.core.result.CommonResult;
import org.example.common.core.usercontext.UserContext;
import org.example.system.entity.vo.UserLoginVO;
import org.example.system.entity.vo.UserVO;
import org.example.system.query.UserQueryPage;
import org.example.system.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param userVO
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public CommonResult<String> register(@Valid @RequestBody UserVO userVO) {
        return CommonResult.success(userService.addUser(userVO));
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @ApiOperation("分页查看用户列表")
    @GetMapping("/list")
    public CommonResult<Page<UserVO>> getUserList(@ModelAttribute UserQueryPage queryPage) {
        return CommonResult.success(userService.getUserList(queryPage));
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @ApiOperation("查看用户详情")
    @GetMapping("/info")
    public CommonResult<UserVO> getUserInfo() {
        return CommonResult.success(userService.getUserInfo(UserContext.get().getId()));
    }

    /**
     * 更新用户信息
     *
     * @param userVO
     * @return
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUser(@RequestBody UserVO userVO) {
        return CommonResult.success(userService.updateUser(userVO));
    }

    /**
     * 更新密码
     *
     * @param userLoginVo
     * @return
     */
    @ApiOperation("更新密码")
    @PostMapping("/updatePassword")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserLoginVO userLoginVo) {
        return CommonResult.success(userService.updateUserPassword(userLoginVo));
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