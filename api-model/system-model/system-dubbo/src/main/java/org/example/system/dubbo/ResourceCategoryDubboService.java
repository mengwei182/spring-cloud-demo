package org.example.system.dubbo;

import org.example.system.vo.ResourceCategoryVO;

public interface ResourceCategoryDubboService {
    /**
     * 根据资源分类名称查询资源分类信息
     *
     * @param name
     * @return
     */
    ResourceCategoryVO getResourceCategoryByName(String name);

    /**
     * 新增资源分类信息
     *
     * @param resourceCategoryVO
     * @return
     */
    ResourceCategoryVO addResourceCategory(ResourceCategoryVO resourceCategoryVO);
}