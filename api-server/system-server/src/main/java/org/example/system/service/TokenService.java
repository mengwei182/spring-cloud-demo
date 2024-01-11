package org.example.system.service;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface TokenService {
    /**
     * 刷新token
     *
     * @param expiration 过期时间
     * @return
     */
    String refresh(Long expiration);
}