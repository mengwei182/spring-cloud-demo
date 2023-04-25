package org.example.dubbo.system;

import org.example.common.entity.system.ResourceCategory;

public interface ResourceCategoryDubboService {
    /**
     * 根据资源分类名称查询资源分类信息
     *
     * @param name
     * @return
     */
    ResourceCategory getResourceCategory(String name);
}