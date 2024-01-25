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
     * 图片验证码
     */
    private String imageCaptcha;
    /**
     * 短信验证码
     */
    private String phoneCaptcha;
    /**
     * 邮箱验证码
     */
    private String emailCaptcha;
}