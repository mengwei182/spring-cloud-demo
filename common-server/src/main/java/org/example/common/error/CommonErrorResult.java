package org.example.common.error;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface CommonErrorResult {
    String OBJECT_NOT_EXIST = "对象不存在";
    String TOKEN_TIME_OUT = "token过期";
    String UNAUTHORIZED = "请求无权限";
}