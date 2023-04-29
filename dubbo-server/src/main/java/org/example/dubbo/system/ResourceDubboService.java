package org.example.dubbo.system;

import org.example.common.entity.system.vo.ResourceVo;

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
    ResourceVo getResource(String url, String categoryId);

    /**
     * 新增资源
     *
     * @param resourceVo
     */
    Boolean addResource(ResourceVo resourceVo);
}