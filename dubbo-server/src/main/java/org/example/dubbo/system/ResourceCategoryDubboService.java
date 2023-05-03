package org.example.dubbo.system;

import org.example.common.entity.system.vo.ResourceCategoryVo;

public interface ResourceCategoryDubboService {
    /**
     * 根据资源分类名称查询资源分类信息
     *
     * @param name
     * @return
     */
    ResourceCategoryVo getResourceCategory(String name);

    /**
     * 新增资源分类
     *
     * @param resourceCategoryVo
     * @return
     */
    ResourceCategoryVo addResourceCategory(ResourceCategoryVo resourceCategoryVo);
}