package org.example.common.model;

import lombok.Data;
import org.example.common.global.ResultCode;

@Data
public class CommonResult {
    private Integer code;
    private String message;
    private Object data;

    private CommonResult() {
    }

    private CommonResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResult success() {
        return new CommonResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), null);
    }

    public static CommonResult success(Object data) {
        return new CommonResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), data);
    }

    public static CommonResult success(String message) {
        return new CommonResult(ResultCode.SUCCESS.getCode(), message, null);
    }

    public static CommonResult success(String message, Object data) {
        return new CommonResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static CommonResult error() {
        return new CommonResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getName(), null);
    }

    public static CommonResult error(Object data) {
        return new CommonResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getName(), data);
    }

    public static CommonResult error(String message) {
        return new CommonResult(ResultCode.ERROR.getCode(), message, null);
    }

    public static CommonResult error(String message, Object data) {
        return new CommonResult(ResultCode.ERROR.getCode(), message, data);
    }

    public static CommonResult unauthorized() {
        return new CommonResult(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getName(), null);
    }

    public static CommonResult forbidden() {
        return new CommonResult(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getName(), null);
    }
}