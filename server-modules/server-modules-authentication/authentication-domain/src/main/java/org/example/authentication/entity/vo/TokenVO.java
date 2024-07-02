package org.example.authentication.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lihui
 * @since 2024/7/2
 */
@Data
@AllArgsConstructor
public class TokenVO {
    /**
     * accessToken用于在需要身份验证的情况下对后端发起请求。
     */
    private String accessToken;
    /**
     * refreshToken用于刷新accessToken。
     */
    private String refreshToken;
}