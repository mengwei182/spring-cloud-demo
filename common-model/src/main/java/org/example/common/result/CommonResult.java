package org.example.common.result;

import lombok.Data;
import org.example.common.global.HttpStatus;

import java.io.Serializable;

/**
 * @author lihui
 * @since 2022/10/26
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
        return new CommonResult<>(HttpStatus.OK.value(), CommonServerResult.SUCCESS, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(HttpStatus.OK.value(), CommonServerResult.SUCCESS, data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), CommonServerResult.ERROR, null);
    }

    public static <T> CommonResult<T> error(T data) {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), CommonServerResult.ERROR, data);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    public static <T> CommonResult<T> unauthorized() {
        return new CommonResult<>(HttpStatus.UNAUTHORIZED.value(), CommonServerResult.UNAUTHORIZED, null);
    }

    public static <T> CommonResult<T> forbidden() {
        return new CommonResult<>(HttpStatus.FORBIDDEN.value(), CommonServerResult.FORBIDDEN, null);
    }
}