package org.example.user.service;

import org.example.user.entity.vo.ResourceCategoryVo;

public interface ResourceCategoryService {
    Boolean addResourceCategory(ResourceCategoryVo resourceCategoryVo);

    Boolean updateResourceCategory(ResourceCategoryVo resourceCategoryVo);
}