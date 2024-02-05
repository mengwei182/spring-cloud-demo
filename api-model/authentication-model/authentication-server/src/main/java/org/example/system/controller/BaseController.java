package org.example.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.model.CommonResult;
import org.example.system.service.BaseService;
import org.example.system.vo.UserLoginVO;
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
@Api(tags = "基础管理")
@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    private BaseService baseService;

    /**
     * 登录
     *
     * @param request
     * @param userLoginVO
     * @return
     */
    @ApiOperation("登录")
    @RequestMapping("/login")
    public CommonResult<String> login(HttpServletRequest request, @Valid @RequestBody UserLoginVO userLoginVO) {
        return CommonResult.success(baseService.login(request, userLoginVO));
    }

    /**
     * 登出
     *
     * @return
     */
    @ApiOperation("登出")
    @RequestMapping("/logout")
    public CommonResult<Boolean> logout() {
        return CommonResult.success(baseService.logout());
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
    @RequestMapping("/image/captcha")
    public void getImageCaptcha(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "130") Integer width, @RequestParam(defaultValue = "30") Integer height, @RequestParam(defaultValue = "4") Integer captchaSize) throws IOException {
        baseService.generateImageCaptcha(request, response, width, height, captchaSize);
    }
}