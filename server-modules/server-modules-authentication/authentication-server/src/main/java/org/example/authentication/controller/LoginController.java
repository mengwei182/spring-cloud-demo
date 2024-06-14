package org.example.authentication.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.authentication.service.LoginService;
import org.example.common.core.result.CommonResult;
import org.example.system.entity.vo.UserLoginVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author lihui
 * @since 2023/4/8
 */
@Api(tags = "登录")
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @param userLoginVO
     * @return
     */
    @ApiOperation("登录")
    @RequestMapping("/login")
    public CommonResult<String> login(@Valid @RequestBody UserLoginVO userLoginVO) {
        return CommonResult.success(loginService.login(userLoginVO));
    }

    /**
     * 登出
     *
     * @return
     */
    @ApiOperation("登出")
    @RequestMapping("/logout")
    public CommonResult<Boolean> logout() {
        return CommonResult.success(loginService.logout());
    }

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @param width 图片宽度
     * @param height 图片高度
     * @param captchaSize 验证码位数
     * @throws IOException
     */
    @ApiOperation("获取图片验证码")
    @RequestMapping("/login/image/captcha")
    public void getImageCaptcha(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "130") Integer width, @RequestParam(defaultValue = "30") Integer height, @RequestParam(defaultValue = "4") Integer captchaSize) throws IOException {
        loginService.generateImageCaptcha(request, response, width, height, captchaSize);
    }
}