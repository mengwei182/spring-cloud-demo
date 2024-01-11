package org.example.system.controller;

import org.example.common.model.CommonResult;
import org.example.system.service.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lihui
 * @since 2023/4/3
 */
@RestController
@RequestMapping("/token")
public class TokenController {
    @Resource
    private TokenService tokenService;

    /**
     * 刷新token
     *
     * @param expiration 过期时间
     * @return
     */
    @RequestMapping("/refresh")
    public CommonResult<String> refresh(@RequestParam Long expiration) {
        return CommonResult.success(tokenService.refresh(expiration));
    }
}