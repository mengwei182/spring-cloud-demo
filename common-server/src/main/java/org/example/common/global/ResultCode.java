package org.example.common.global;

public enum ResultCode {
    SUCCESS(200, "SUCCESS"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    ERROR(500, "ERROR");
    private final Integer code;
    private final String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    ResultCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}