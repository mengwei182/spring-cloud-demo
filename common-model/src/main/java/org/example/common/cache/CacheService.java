package org.example.common.cache;

/**
 * @author lihui
 * @since 2024/1/23
 */
public interface CacheService {
    /**
     * 清理全部缓存
     *
     * @param ids 可以关联到缓存的关键字，如userId
     */
    void clear(Object... ids);
}