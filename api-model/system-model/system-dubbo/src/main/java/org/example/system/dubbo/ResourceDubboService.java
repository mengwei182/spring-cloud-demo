package org.example.system.dubbo;

import org.example.system.vo.ResourceVO;

import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
public interface ResourceDubboService {
    /**
     * 根据url和分类id查询资源信息
     *
     * @param url
     * @param categoryId
     * @return
     */
    ResourceVO getResource(String url, String categoryId);

    /**
     * 新增资源
     *
     * @param resourceVO
     */
    Boolean addResource(ResourceVO resourceVO);

    /**
     * 获取用户所属资源
     *
     * @param userId
     * @return
     */
    List<ResourceVO> getResourceByUserId(String userId);

    /**
     * 清理用户资源缓存
     *
     * @param userId
     */
    void clearResourceCache(String userId);
}