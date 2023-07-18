package org.example.common.error;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface CommonServerResult {
    String SUCCESS = "成功";
    String ERROR = "错误";
    String OBJECT_NOT_EXIST = "对象不存在";
    String TOKEN_TIME_OUT = "token过期";
    String UNAUTHORIZED = "访问无权限";
    String FORBIDDEN = "访问被拒绝";
    String NAME_NOT_NULL = "名称不能为空";
    String NAME_LENGTH_ERROR = "名称长度不能超过255个字符";
    String ID_LENGTH_ERROR = "主键长度不能超过32个字符";
}