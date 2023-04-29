package org.example.common.error;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface SystemServerErrorResult {
    String USERNAME_NULL = "用户名为空";
    String PASSWORD_NULL = "密码为空";
    String USER_EXIST = "用户已存在";
    String USER_NOT_EXIST = "用户不存在";
    String PASSWORD_ERROR = "密码错误";
    String VERIFY_CODE_ERROR = "验证码错误";
    String VERIFY_CODE_OVERDUE = "验证码过期";
    String MENU_NAME_EXIST = "存在同名菜单";
    String PARENT_NOT_EXIST = "父级对象不存在";
    String CATEGORY_NOT_EXIST = "分类不存在";
    String CATEGORY_EXIST = "分类已存在";
    String RESOURCE_NOT_EXIST = "资源为空";
    String CATEGORY_RESOURCE_EXIST = "分类下存在资源";
    String RESOURCE_NAME_DUPLICATE = "资源名称重复";
    String RESOURCE_URL_NOT_NULL = "资源URL不能为空";
    String RESOURCE_URL_LENGTH_ERROR = "资源URL长度不能超过255个字符";
    String RESOURCE_CATEGORY_ID_NOT_NULL = "资源分类不能为空";
    String RESOURCE_CATEGORY_ID_LENGTH_ERROR = "资源分类主键不能超过32个字符";
    String ROLE_NAME_EXIST = "存在同名角色";
    String DICTIONARY_NAME_EXIST = "存在同名字典";
    String DICTIONARY_CODE_EXIST = "存在相同编码字典";
    String DICTIONARY_CHILD_EXIST = "存在子字典";
}