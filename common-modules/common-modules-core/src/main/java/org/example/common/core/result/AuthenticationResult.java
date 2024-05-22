package org.example.common.core.result;

/**
 * @author lihui
 * @since 2024/5/21
 */
public interface AuthenticationResult {
    String TOKEN_TIME_OUT = "token过期";
    String UNAUTHORIZED = "访问无权限";
    String FORBIDDEN = "访问被拒绝";
    String TOKEN_REFRESH_FAIL = "Token刷新失败";
    String TOKEN_VALID = "Token无效";
}