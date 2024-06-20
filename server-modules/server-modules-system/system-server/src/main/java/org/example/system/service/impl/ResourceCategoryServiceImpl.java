package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.PageUtils;
import org.example.system.dubbo.ResourceCategoryDubboService;
import org.example.system.entity.Resource;
import org.example.system.entity.ResourceCategory;
import org.example.system.entity.vo.ResourceCategoryVO;
import org.example.system.entity.vo.ResourceVO;
import org.example.system.exception.SystemException;
import org.example.system.mapper.ResourceCategoryMapper;
import org.example.system.mapper.ResourceMapper;
import org.example.system.entity.query.ResourceCategoryQueryPage;
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
     * @param resourceCategoryVO
     * @return
     */
    @Override
    public ResourceCategoryVO addResourceCategory(ResourceCategoryVO resourceCategoryVO) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResourceCategory::getName, resourceCategoryVO.getName());
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(queryWrapper);
        if (resourceCategory != null) {
            throw new SystemException(ExceptionInformation.SYSTEM_3004.getCode(), ExceptionInformation.SYSTEM_3004.getMessage());
        }
        resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVO, resourceCategory);
        resourceCategory.setId(CommonUtils.uuid());
        resourceCategoryMapper.insert(resourceCategory);
        resourceCategoryVO.setId(resourceCategory.getId());
        return resourceCategoryVO;
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
            throw new SystemException(ExceptionInformation.SYSTEM_3003.getCode(), ExceptionInformation.SYSTEM_3003.getMessage());
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, id));
        if (count != null && count > 0) {
            throw new SystemException(ExceptionInformation.SYSTEM_3006.getCode(), ExceptionInformation.SYSTEM_3006.getMessage());
        }
        resourceCategoryMapper.deleteById(id);
        return true;
    }

    /**
     * 更新资源分类
     *
     * @param resourceCategoryVO
     * @return
     */
    @Override
    public Boolean updateResourceCategory(ResourceCategoryVO resourceCategoryVO) {
        QueryWrapper<ResourceCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ResourceCategory::getName, resourceCategoryVO.getName());
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(queryWrapper);
        if (resourceCategory != null) {
            throw new SystemException(ExceptionInformation.SYSTEM_3004.getCode(), ExceptionInformation.SYSTEM_3004.getMessage());
        }
        resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryVO, resourceCategory);
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
    public Page<ResourceCategoryVO> getResourceCategoryList(ResourceCategoryQueryPage queryPage) {
        Page<ResourceCategory> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<ResourceCategory> resourceCategories = resourceCategoryMapper.getResourceCategoryList(page, queryPage);
        page.setRecords(resourceCategories);
        Page<ResourceCategoryVO> resltPage = PageUtils.wrap(page, ResourceCategoryVO.class);
        List<ResourceCategoryVO> records = resltPage.getRecords();
        for (ResourceCategoryVO record : records) {
            List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, record.getId()));
            record.setResources(CommonUtils.transformList(resources, ResourceVO.class));
        }
        return resltPage;
    }

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    @Override
    public List<ResourceCategoryVO> getAllResourceCategoryList() {
        List<ResourceCategory> resourceCategories = resourceCategoryMapper.selectList(new LambdaQueryWrapper<>());
        List<ResourceCategoryVO> resourceCategoryVos = CommonUtils.transformList(resourceCategories, ResourceCategoryVO.class);
        for (ResourceCategoryVO resourceCategoryVO : resourceCategoryVos) {
            List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceCategoryVO.getId()));
            resourceCategoryVO.setResources(CommonUtils.transformList(resources, ResourceVO.class));
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
    public ResourceCategoryVO getResourceCategoryByName(String name) {
        ResourceCategory resourceCategory = resourceCategoryMapper.selectOne(new LambdaQueryWrapper<ResourceCategory>().eq(ResourceCategory::getName, name));
        return CommonUtils.transformObject(resourceCategory, ResourceCategoryVO.class);
    }
}