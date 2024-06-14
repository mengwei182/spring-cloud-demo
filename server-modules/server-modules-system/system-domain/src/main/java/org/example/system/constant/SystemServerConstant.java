package org.example.system.constant;

/**
 * @author lihui
 * @since 2022/11/8
 */
public interface SystemServerConstant {
    String USER_TOKEN_KEY = "USER_TOKEN_KEY::";
    String RESOURCE_KEY = "RESOURCE_KEY::";
    String RESOURCE_URL_NOT_NULL = "资源URL不能为空";
    String RESOURCE_URL_LENGTH_ERROR = "资源URL长度不能超过255个字符";
    String RESOURCE_CATEGORY_ID_NOT_NULL = "资源分类不能为空";
    String RESOURCE_CATEGORY_ID_LENGTH_ERROR = "资源分类主键不能超过32个字符";
    String PATH_NOT_NULL = "路由地址不能为空";
    String PATH_LENGTH_ERROR = "路由地址不能超过255个字符";
}