package org.example.common.global;

import lombok.Getter;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "SUCCESS"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    ERROR(500, "ERROR");
    private final Integer code;
    private final String name;

    ResultCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}