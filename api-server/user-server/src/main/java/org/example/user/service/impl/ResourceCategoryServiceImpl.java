package org.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.common.error.UserServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.util.CommonUtils;
import org.example.user.entity.ResourceCategory;
import org.example.user.entity.vo.ResourceCategoryVO;
import org.example.user.mapper.ResourceCategoryMapper;
import org.example.user.service.ResourceCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ResourceCategoryServiceImpl implements ResourceCategoryService {
    @Resource
    private ResourceCategoryMapper resourceCategoryMapper;

    @Override
    public Boolean addResourceCategory(ResourceCategoryVO resourceCategoryVo) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResourceCategory::getName, resourceCategoryVo.getName());
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(queryWrapper);
        if (resourceCategory != null) {
            throw new CommonException(UserServerErrorResult.CATEGORY_EXIST);
        }
        resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVo, resourceCategory);
        resourceCategory.setId(CommonUtils.uuid());
        resourceCategoryMapper.insert(resourceCategory);
        return true;
    }

    @Override
    public Boolean updateResourceCategory(ResourceCategoryVO resourceCategoryVo) {
        ResourceCategory resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVo, resourceCategory);
        resourceCategoryMapper.updateById(resourceCategory);
        return true;
    }
}