package org.example.common.core.result;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface CommonServerResult {
    String SUCCESS = "成功";
    String ERROR = "错误";
    String SYSTEM_ERROR = "系统错误";
    String OBJECT_NOT_EXIST = "对象不存在";
    String TOKEN_TIME_OUT = "token过期";
    String UNAUTHORIZED = "访问无权限";
    String FORBIDDEN = "访问被拒绝";
    String NAME_NOT_NULL = "名称不能为空";
    String NAME_LENGTH_ERROR = "名称长度不能超过255个字符";
    String ID_LENGTH_ERROR = "主键长度不能超过32个字符";
    String AUTHORIZATION = "Authorization";
    String PROFILE_PROD = "prod";

    String COMMON_EXCEPTION_CODE = "100000";
    String SYSTEM_EXCEPTION_CODE = "100001";
    String AUTHENTICATION_EXCEPTION_CODE = "100002";

    String LOGIN = "login_";
    String REGISTER = "register_";
    String FORGOT_PASSWORD = "forgot_password_";
}