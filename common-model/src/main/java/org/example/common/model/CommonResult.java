package org.example.common.model;

import lombok.Data;
import org.example.common.global.ResultCode;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    private CommonResult() {
    }

    private CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getName(), null);
    }

    public static <T> CommonResult<T> error(T data) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getName(), data);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), message, null);
    }

    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), message, data);
    }

    public static <T> CommonResult<T> unauthorized() {
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getName(), null);
    }

    public static <T> CommonResult<T> forbidden() {
        return new CommonResult<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getName(), null);
    }
}