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
     * 1.图片验证码
     * 2.短信验证码
     * 3.公私钥验证
     * 4.人脸验证
     * 5.指纹验证
     * 6.手势验证
     * 7.邮箱验证
     */
    NULL(0), IMAGE(1), PHONE(2), SECRET_KEY(3), FACE(4), FINGERPRINT(5), GESTURE(6), EMAIL(7);
    private final int status;

    UserVerifyStatusEnum(int status) {
        this.status = status;
    }
}