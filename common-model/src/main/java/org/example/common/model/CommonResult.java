package org.example.common.model;

import lombok.Data;
import org.example.common.result.CommonServerResult;
import org.example.common.global.ResultCode;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
public class CommonResult<T> implements Serializable {
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
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), CommonServerResult.SUCCESS, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), CommonServerResult.SUCCESS, data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(ResultCode.ERROR.getCode(), CommonServerResult.ERROR, null);
    }

    public static <T> CommonResult<T> error(T data) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), CommonServerResult.ERROR, data);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), message, null);
    }

    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<>(ResultCode.ERROR.getCode(), message, data);
    }

    public static <T> CommonResult<T> unauthorized() {
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode(), CommonServerResult.UNAUTHORIZED, null);
    }

    public static <T> CommonResult<T> forbidden() {
        return new CommonResult<>(ResultCode.FORBIDDEN.getCode(), CommonServerResult.FORBIDDEN, null);
    }
}