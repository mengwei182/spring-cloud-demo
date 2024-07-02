package org.example.authentication.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.authentication.entity.vo.TokenVO;
import org.example.authentication.service.TokenService;
import org.example.common.core.result.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Api(tags = "Token管理")
@RestController
@RequestMapping("/token")
public class TokenController {
    @Resource
    private TokenService tokenService;

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @ApiOperation("刷新token")
    @PostMapping(value = "/refresh")
    public CommonResult<TokenVO> refresh(String refreshToken) {
        return CommonResult.success(tokenService.refresh(refreshToken));
    }
}