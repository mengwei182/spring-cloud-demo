package org.example.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.CaffeineRedisCache;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.PageUtils;
import org.example.common.redis.Topic;
import org.example.system.constant.SystemServerConstant;
import org.example.system.dubbo.ResourceDubboService;
import org.example.system.entity.Resource;
import org.example.system.entity.ResourceCategory;
import org.example.system.entity.RoleResourceRelation;
import org.example.system.entity.UserRoleRelation;
import org.example.system.entity.vo.ResourceVO;
import org.example.system.exception.SystemException;
import org.example.system.mapper.ResourceCategoryMapper;
import org.example.system.mapper.ResourceMapper;
import org.example.system.mapper.RoleResourceRelationMapper;
import org.example.system.mapper.UserRoleRelationMapper;
import org.example.system.query.ResourceQueryPage;
import org.example.system.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
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
    private UserRoleRelationMapper userRoleRelationMapper;
    @javax.annotation.Resource
    private CaffeineRedisCache caffeineRedisCache;

    /**
     * 新增资源
     *
     * @param resourceVO
     * @return
     */
    @Override
    public Boolean addResource(ResourceVO resourceVO) {
        ResourceCategory resourceCategory = resourceCategoryMapper.selectById(resourceVO.getCategoryId());
        if (resourceCategory == null) {
            throw new SystemException(ExceptionInformation.SYSTEM_3003.getCode(), ExceptionInformation.SYSTEM_3003.getMessage());
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceVO.getCategoryId()).eq(Resource::getName, resourceVO.getName()));
        if (count != null && count > 0) {
            throw new SystemException(ExceptionInformation.SYSTEM_3007.getCode(), ExceptionInformation.SYSTEM_3007.getMessage());
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceVO, resource);
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
            throw new SystemException(ExceptionInformation.SYSTEM_3005.getCode(), ExceptionInformation.SYSTEM_3005.getMessage());
        }
        // 删除角色资源表的关联信息
        roleResourceRelationMapper.delete(new LambdaQueryWrapper<RoleResourceRelation>().eq(RoleResourceRelation::getResourceId, id));
        resourceMapper.deleteById(id);
        return true;
    }

    /**
     * 更新资源
     *
     * @param resourceVO
     * @return
     */
    @Override
    public Boolean updateResource(ResourceVO resourceVO) {
        Resource resource = resourceMapper.selectById(resourceVO.getId());
        if (resource == null) {
            throw new SystemException(ExceptionInformation.SYSTEM_3005.getCode(), ExceptionInformation.SYSTEM_3005.getMessage());
        }
        ResourceCategory resourceCategory = resourceCategoryMapper.selectById(resourceVO.getCategoryId());
        if (resourceCategory == null) {
            throw new SystemException(ExceptionInformation.SYSTEM_3003.getCode(), ExceptionInformation.SYSTEM_3003.getMessage());
        }
        Long count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>().eq(Resource::getCategoryId, resourceVO.getCategoryId()).eq(Resource::getName, resourceVO.getName()));
        if (count != null && count > 0) {
            throw new SystemException(ExceptionInformation.SYSTEM_3007.getCode(), ExceptionInformation.SYSTEM_3007.getMessage());
        }
        Resource insterResource = new Resource();
        BeanUtils.copyProperties(resourceVO, insterResource);
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
    public Page<ResourceVO> getResourceList(ResourceQueryPage queryPage) {
        Page<Resource> page = new Page<>();
        List<Resource> resourceList = resourceMapper.getResourceList(page, queryPage);
        page.setRecords(resourceList);
        return PageUtils.wrap(page, ResourceVO.class);
    }

    /**
     * 获取全部资源列表
     *
     * @return
     */
    @Override
    public List<ResourceVO> getAllResourceList() {
        List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<>());
        return CommonUtils.transformList(resources, ResourceVO.class);
    }

    /**
     * 根据id获取资源详情
     *
     * @return
     */
    @Override
    public ResourceVO getResourceById(String id) {
        Resource resource = resourceMapper.selectById(id);
        ResourceVO resourceVO = new ResourceVO();
        BeanUtils.copyProperties(resource, resourceVO);
        return resourceVO;
    }

    /**
     * 根据url和分类id查询资源信息
     *
     * @param url
     * @param categoryId
     * @return
     */
    @Override
    public ResourceVO getResource(String url, String categoryId) {
        Resource resource = resourceMapper.selectOne(new LambdaQueryWrapper<Resource>().eq(Resource::getUrl, url).eq(Resource::getCategoryId, categoryId));
        return CommonUtils.transformObject(resource, ResourceVO.class);
    }

    /**
     * 刷新所有系统中所有资源
     */
    @Override
    public void refreshResource() {
        caffeineRedisCache.convertAndSend(Topic.REFRESH_RESOURCE_TOPIC, Boolean.TRUE);
    }

    /**
     * 根据用户id获取资源列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<ResourceVO> getResourceByUserId(String userId) {
        String key = SystemServerConstant.RESOURCE_KEY + userId;
        List<ResourceVO> resourceVOs = (List<ResourceVO>) caffeineRedisCache.get(key, List.class);
        if (CollectionUtil.isEmpty(resourceVOs)) {
            List<UserRoleRelation> userRoleRelations = userRoleRelationMapper.selectList(new LambdaQueryWrapper<UserRoleRelation>().eq(UserRoleRelation::getUserId, userId));
            if (CollectionUtil.isEmpty(userRoleRelations)) {
                return resourceVOs;
            }
            List<RoleResourceRelation> roleResourceRelations = roleResourceRelationMapper.selectList(new LambdaQueryWrapper<RoleResourceRelation>().in(RoleResourceRelation::getRoleId, userRoleRelations.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList())));
            if (CollectionUtil.isEmpty(roleResourceRelations)) {
                return resourceVOs;
            }
            List<Resource> resources = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().in(Resource::getId, roleResourceRelations.stream().map(RoleResourceRelation::getResourceId).collect(Collectors.toList())));
            resourceVOs = CommonUtils.transformList(resources, ResourceVO.class);
        }
        caffeineRedisCache.put(key, resourceVOs, Duration.ofHours(1));
        return resourceVOs;
    }

    /**
     * 清理用户资源缓存
     *
     * @param userId
     */
    @Override
    public void clearResourceCache(String userId) {
        caffeineRedisCache.evict(SystemServerConstant.RESOURCE_KEY + userId);
    }
}