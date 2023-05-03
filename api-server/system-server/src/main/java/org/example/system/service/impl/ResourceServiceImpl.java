package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.entity.system.Resource;
import org.example.common.entity.system.ResourceCategory;
import org.example.common.entity.system.RoleResourceRelation;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.error.SystemServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.model.CommonResult;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.dubbo.system.ResourceDubboService;
import org.example.system.api.ResourceQueryPage;
import org.example.system.mapper.ResourceCategoryMapper;
import org.example.system.mapper.ResourceMapper;
import org.example.system.mapper.RoleResourceRelationMapper;
import org.example.system.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = ResourceDubboService.class)
public class ResourceServiceImpl implements ResourceService, ResourceDubboService {
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;
    @javax.annotation.Resource
    private ResourceCategoryMapper resourceCategoryMapper;
    @javax.annotation.Resource
    private RoleResourceRelationMapper roleResourceRelationMapper;
    @javax.annotation.Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 新增资源
     *
     * @param resourceVo
     * @return
     */
    @Override
    public Boolean addResource(ResourceVo resourceVo) {
        ResourceCategory resourceCategory = resourceCategoryMapper.selectById(resourceVo.getCategoryId());
        if (resourceCategory == null) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_NOT_EXIST);
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceVo.getCategoryId()).eq(Resource::getName, resourceVo.getName()));
        if (count != null && count > 0) {
            throw new CommonException(SystemServerErrorResult.RESOURCE_NAME_DUPLICATE);
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceVo, resource);
        resource.setId(CommonUtils.uuid());
        resourceMapper.insert(resource);
        return true;
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteResource(String id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource == null) {
            throw new CommonException(SystemServerErrorResult.RESOURCE_NOT_EXIST);
        }
        // 删除角色资源表的关联信息
        roleResourceRelationMapper.delete(new LambdaQueryWrapper<RoleResourceRelation>().eq(RoleResourceRelation::getResourceId, id));
        resourceMapper.deleteById(id);
        return true;
    }

    /**
     * 更新资源
     *
     * @param resourceVo
     * @return
     */
    @Override
    public Boolean updateResource(ResourceVo resourceVo) {
        Resource resource = resourceMapper.selectById(resourceVo.getId());
        if (resource == null) {
            throw new CommonException(SystemServerErrorResult.RESOURCE_NOT_EXIST);
        }
        ResourceCategory resourceCategory = resourceCategoryMapper.selectById(resourceVo.getCategoryId());
        if (resourceCategory == null) {
            throw new CommonException(SystemServerErrorResult.CATEGORY_NOT_EXIST);
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceVo.getCategoryId()).eq(Resource::getName, resourceVo.getName()));
        if (count != null && count > 0) {
            throw new CommonException(SystemServerErrorResult.RESOURCE_NAME_DUPLICATE);
        }
        Resource insterResource = new Resource();
        BeanUtils.copyProperties(resourceVo, insterResource);
        resourceMapper.updateById(insterResource);
        return true;
    }

    /**
     * 分页获取资源列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<ResourceVo> getResourceList(ResourceQueryPage queryPage) {
        Page<Resource> page = new Page<>();
        List<Resource> resourceList = resourceMapper.getResourceList(page, queryPage);
        page.setRecords(resourceList);
        return PageUtils.wrap(page, ResourceVo.class);
    }

    /**
     * 获取全部资源列表
     *
     * @return
     */
    @Override
    public List<ResourceVo> getAllResourceList() {
        List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<>());
        return CommonUtils.transformList(resources, ResourceVo.class);
    }

    /**
     * 根据id获取资源详情
     *
     * @return
     */
    @Override
    public ResourceVo getResourceById(String id) {
        Resource resource = resourceMapper.selectById(id);
        ResourceVo resourceVo = new ResourceVo();
        BeanUtils.copyProperties(resource, resourceVo);
        return resourceVo;
    }

    /**
     * 刷新所有系统中所有资源
     */
    @Override
    public void refreshResource() {
        CommonResult commonResult = CommonResult.success();
        commonResult.setData(Boolean.TRUE);
        redisTemplate.convertAndSend("refresh_resource_topic", commonResult);
    }

    /**
     * 根据url和分类id查询资源信息
     *
     * @param url
     * @param categoryId
     * @return
     */
    @Override
    public ResourceVo getResource(String url, String categoryId) {
        Resource resource = resourceMapper.selectOne(new LambdaQueryWrapper<Resource>().eq(Resource::getUrl, url).eq(Resource::getCategoryId, categoryId));
        return CommonUtils.transformObject(resource, ResourceVo.class);
    }
}