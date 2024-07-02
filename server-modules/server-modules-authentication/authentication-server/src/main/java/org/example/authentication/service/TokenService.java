package org.example.authentication.service;

import org.example.authentication.entity.vo.TokenVO;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface TokenService {
    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    TokenVO refresh(String refreshToken);
}