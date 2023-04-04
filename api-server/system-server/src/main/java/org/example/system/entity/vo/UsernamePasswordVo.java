package org.example.system.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsernamePasswordVo {
    private String id;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机验证码
     */
    private String phoneVerifyCode;
    /**
     * 图形验证码
     */
    private String imageVerifyCode;
}