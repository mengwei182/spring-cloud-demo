package org.example.system.controller;

import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.model.CommonResult;
import org.example.system.service.BaseService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author lihui
 * @since 2023/4/8
 */
@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    private BaseService baseService;

    /**
     * 登录
     *
     * @param usernamePasswordVo
     * @return
     */
    @RequestMapping("/login")
    public CommonResult login(@Valid @RequestBody UsernamePasswordVo usernamePasswordVo) {
        return CommonResult.success(baseService.login(usernamePasswordVo));
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success(baseService.logout());
    }

    /**
     * 获取图片验证码
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/image/verify/code")
    public void getImageVerifyCode(HttpServletResponse response) throws IOException {
        baseService.generateImageVerifyCode(response);
    }
}