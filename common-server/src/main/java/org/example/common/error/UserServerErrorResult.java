package org.example.common.error;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface UserServerErrorResult {
    String USERNAME_NULL = "用户名为空";
    String PASSWORD_NULL = "密码为空";
    String USER_EXIST = "用户已存在";
    String USER_NOT_EXIST = "用户不存在";
    String PASSWORD_ERROR = "密码错误";
    String VERIFY_CODE_ERROR = "验证码错误";
    String VERIFY_CODE_OVERDUE = "验证码过期";
    String CATEGORY_EXIST = "分类已存在";
    String MENU_NAME_EXIST = "存在同名菜单";
    String PARENT_NOT_EXIST = "父级对象不存在";
}