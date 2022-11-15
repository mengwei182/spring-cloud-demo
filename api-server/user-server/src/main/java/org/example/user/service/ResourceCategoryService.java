package org.example.user.service;

import org.example.user.entity.vo.ResourceCategoryVO;

public interface ResourceCategoryService {
    Boolean addResourceCategory(ResourceCategoryVO resourceCategoryVo);

    Boolean updateResourceCategory(ResourceCategoryVO resourceCategoryVo);
}