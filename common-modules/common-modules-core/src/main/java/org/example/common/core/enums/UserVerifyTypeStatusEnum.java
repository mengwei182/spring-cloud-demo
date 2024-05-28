package org.example.common.core.enums;

import lombok.Getter;

/**
 * @author lihui
 * @since 2024/1/22
 */
@Getter
public enum UserVerifyTypeStatusEnum {
    NULL(0, "关闭验证"),
    USERNAME_PASSWORD(1, "用户名密码"),
    IMAGE_CAPTCHA(2, "图片验证码"),
    PHONE_CAPTCHA(3, "短信验证码"),
    SECRET_KEY(4, "公私钥验证"),
    FACE(5, "人脸识别"),
    FINGERPRINT(6, "指纹识别"),
    GESTURE(7, "手势密码"),
    EMAIL_CAPTCHA(8, "邮箱验证码");

    private final int type;
    private final String describe;

    UserVerifyTypeStatusEnum(int type, String describe) {
        this.type = type;
        this.describe = describe;
    }
}