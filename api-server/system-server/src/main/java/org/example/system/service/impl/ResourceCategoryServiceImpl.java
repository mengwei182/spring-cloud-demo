package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
org.example.common.entity.system.Resource;
org.example.common.entity.system.ResourceCategory;
org.example.common.entity.system.vo.ResourceCategoryVo;
org.example.common.entity.system.vo.ResourceVo;
import org.example.common.error.SystemServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.dubbo.system.ResourceCategoryDubboService;
import org.example.system.api.ResourceCategoryQueryPage;
import org.example.system.mapper.ResourceCategoryMapper;
import org.example.system.mapper.ResourceMapper;
import org.example.system.service.ResourceCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = ResourceCategoryDubboService.class)
public class ResourceCategoryServiceImpl implements ResourceCategoryService, ResourceCategoryDubboService {
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;
    @javax.annotation.Resource
    private ResourceCategoryMapper resourceCategoryMapper;

    /**
     * 新增资源分类信息
     *
     * @param resourceCategoryVo
     * @return
     */
    @Override
    public Boolean addResourceCategory(ResourceCategoryVo resourceCategoryVo) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResourceCategory::getName, resourceCategoryVo.getName());
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(queryWrapper);
        if (resourceCategory != null) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_EXIST);
        }
        resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVo, resourceCategory);
        resourceCategory.setId(CommonUtils.uuid());
        resourceCategoryMapper.insert(resourceCategory);
        return true;
    }

    /**
     * 删除资源分类信息
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteResourceCategory(String id) {
        ResourceCategory resourceCategory = resourceCategoryMapper.selectById(id);
        if (resourceCategory == null) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_NOT_EXIST);
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, id));
        if (count != null && count > 0) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_RESOURCE_EXIST);
        }
        resourceCategoryMapper.deleteById(id);
        return true;
    }

    /**
     * 更新资源分类
     *
     * @param resourceCategoryVo
     * @return
     */
    @Override
    public Boolean updateResourceCategory(ResourceCategoryVo resourceCategoryVo) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResourceCategory::getName, resourceCategoryVo.getName());
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(queryWrapper);
        if (resourceCategory != null) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_EXIST);
        }
        resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVo, resourceCategory);
        resourceCategoryMapper.updateById(resourceCategory);
        return true;
    }

    /**
     * 获取资源分类列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<ResourceCategoryVo> getResourceCategoryList(ResourceCategoryQueryPage queryPage) {
        Page<ResourceCategory> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<ResourceCategory> resourceCategories = resourceCategoryMapper.getResourceCategoryList(page, queryPage);
        page.setRecords(resourceCategories);
        Page<ResourceCategoryVo> resltPage = PageUtils.wrap(page, ResourceCategoryVo.class);
        List<ResourceCategoryVo> records = resltPage.getRecords();
        for (ResourceCategoryVo record : records) {
            List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, record.getId()));
            record.setResources(CommonUtils.transformList(resources, ResourceVo.class));
        }
        return resltPage;
    }

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    @Override
    public List<ResourceCategoryVo> getAllResourceCategoryList() {
        List<ResourceCategory> resourceCategories = resourceCategoryMapper.selectList(new LambdaQueryWrapper<>());
        List<ResourceCategoryVo> resourceCategoryVos = CommonUtils.transformList(resourceCategories, ResourceCategoryVo.class);
        for (ResourceCategoryVo resourceCategoryVo : resourceCategoryVos) {
            List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceCategoryVo.getId()));
            resourceCategoryVo.setResources(CommonUtils.transformList(resources, ResourceVo.class));
        }
        return resourceCategoryVos;
    }

    /**
     * 根据资源分类名称查询资源分类信息
     *
     * @param name
     * @return
     */
    @Override
    public ResourceCategoryVo getResourceCategory(String name) {
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(new LambdaQueryWrapper<ResourceCategory>().eq(ResourceCategory::getName, name));
        return CommonUtils.transformObject(resourceCategory, ResourceCategoryVo.class);
    }
}