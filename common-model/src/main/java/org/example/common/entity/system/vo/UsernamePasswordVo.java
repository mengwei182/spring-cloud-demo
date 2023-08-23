package org.example.common.entity.system.vo;

import lombok.Data;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
public class UsernamePasswordVo {
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 验证码
     */
    private String captcha;
}