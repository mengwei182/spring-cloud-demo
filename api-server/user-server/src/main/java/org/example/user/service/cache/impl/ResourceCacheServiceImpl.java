package org.example.user.service.cache.impl;

import org.example.common.util.CommonUtils;
import org.example.user.entity.Resource;
import org.example.user.mapper.ResourceMapper;
import org.example.user.service.cache.ResourceCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lihui
 * @since 2022/10/30
 */
@Service
public class ResourceCacheServiceImpl implements ResourceCacheService {
    @javax.annotation.Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;
    private static final String RESOURCE_CACHE = "RESOURCE_CACHE";

    @Override
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        Set<Object> members = redisTemplate.opsForSet().members(RESOURCE_CACHE);
        if (!CollectionUtils.isEmpty(members)) {
            for (Object member : members) {
                resources.add(CommonUtils.gson().fromJson(CommonUtils.gson().toJson(member), Resource.class));
            }
        } else {
            resources = resourceMapper.selectList(null);
            redisTemplate.opsForSet().add(RESOURCE_CACHE, resources.toArray());
        }
        return resources;
    }

    @Override
    public void setResource(List<Resource> resources) {
        redisTemplate.opsForSet().add(RESOURCE_CACHE, resources.toArray());
    }
}