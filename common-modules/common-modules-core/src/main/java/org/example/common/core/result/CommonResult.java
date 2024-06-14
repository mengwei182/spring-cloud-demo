package org.example.common.core.result;

import lombok.Data;
import org.example.common.core.constant.CommonConstant;
import org.example.common.core.constant.HttpStatus;
import org.example.common.core.exception.ExceptionInformation;

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
        return new CommonResult<>(HttpStatus.OK.value(), CommonConstant.SUCCESS, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(HttpStatus.OK.value(), CommonConstant.SUCCESS, data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(ExceptionInformation.EXCEPTION_1000.getCode(), ExceptionInformation.EXCEPTION_1000.getMessage(), null);
    }

    public static <T> CommonResult<T> error(T data) {
        return new CommonResult<>(ExceptionInformation.EXCEPTION_1000.getCode(), ExceptionInformation.EXCEPTION_1000.getMessage(), data);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(ExceptionInformation.EXCEPTION_1000.getCode(), message, null);
    }

    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<>(ExceptionInformation.EXCEPTION_1000.getCode(), message, data);
    }

    public static <T> CommonResult<T> unauthorized() {
        return new CommonResult<>(ExceptionInformation.AUTHENTICATION_2001.getCode(), ExceptionInformation.AUTHENTICATION_2001.getMessage(), null);
    }

    public static <T> CommonResult<T> forbidden() {
        return new CommonResult<>(ExceptionInformation.AUTHENTICATION_2002.getCode(), ExceptionInformation.AUTHENTICATION_2002.getMessage(), null);
    }

    public static <T> CommonResult<T> result(Integer code, String message, T data) {
        return new CommonResult<>(code, message, data);
    }
}