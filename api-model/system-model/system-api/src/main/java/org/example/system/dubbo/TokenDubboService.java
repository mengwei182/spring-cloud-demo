package org.example.system.dubbo;

/**
 * @author lihui
 * @since 2024/2/5
 */
public interface TokenDubboService {
    /**
     * 清理用户token缓存
     *
     * @param userId
     */
    void clearTokenCache(String userId);
}