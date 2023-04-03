package org.example.system.service;

import org.example.system.entity.vo.ResourceCategoryVo;

public interface ResourceCategoryService {
    Boolean addResourceCategory(ResourceCategoryVo resourceCategoryVo);

    Boolean updateResourceCategory(ResourceCategoryVo resourceCategoryVo);
}