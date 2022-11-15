package org.example.user.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsernamePasswordVo {
    private String id;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    private String phone;
    private String phoneVerifyCode;
    private String imageVerifyCode;
}