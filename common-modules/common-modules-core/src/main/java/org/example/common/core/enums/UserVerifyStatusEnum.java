package org.example.common.core.enums;

import lombok.Getter;

/**
 * @author lihui
 * @since 2024/1/22
 */
@Getter
public enum UserVerifyStatusEnum {
    /**
     * 其他验证状态
     * 0.关闭
     * 1.账号密码
     * 2.图片验证码
     * 3.短信验证码
     * 4.公私钥验证
     * 5.人脸验证
     * 6.指纹验证
     * 7.手势验证
     * 8.邮箱验证
     */
    NULL(0), USERNAME_PASSWORD(1), IMAGE_CAPTCHA(2), PHONE_CAPTCHA(3), SECRET_KEY(4), FACE(5), FINGERPRINT(6), GESTURE(7), EMAIL_CAPTCHA(8);
    private final int status;

    UserVerifyStatusEnum(int status) {
        this.status = status;
    }
}