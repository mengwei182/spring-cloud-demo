package org.example.system.service.cache.impl;

import org.example.common.util.CommonUtils;
import org.example.system.entity.RoleResourceRelation;
import org.example.system.mapper.RoleResourceRelationMapper;
import org.example.system.service.cache.RoleResourceRelationCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Service
public class RoleResourceRelationCacheServiceImpl implements RoleResourceRelationCacheService {
    private static final String ROLE_RESOURCE_RELATION_CACHE = "ROLE_RESOURCE_RELATION_CACHE";
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private RoleResourceRelationMapper roleResourceRelationMapper;

    @Override
    public List<RoleResourceRelation> getRoleResourceRelations() {
        List<RoleResourceRelation> roleResourceRelations = new ArrayList<>();
        Set<Object> members = redisTemplate.opsForSet().members(ROLE_RESOURCE_RELATION_CACHE);
        if (!CollectionUtils.isEmpty(members)) {
            for (Object member : members) {
                roleResourceRelations.add(CommonUtils.gson().fromJson(CommonUtils.gson().toJson(member), RoleResourceRelation.class));
            }
        } else {
            roleResourceRelations = roleResourceRelationMapper.selectList(null);
            redisTemplate.opsForSet().add(ROLE_RESOURCE_RELATION_CACHE, roleResourceRelations.toArray());
        }
        return roleResourceRelations;
    }

    @Override
    public void setRoleResourceRelations(List<RoleResourceRelation> roleResourceRelations) {
        redisTemplate.opsForSet().add(ROLE_RESOURCE_RELATION_CACHE, roleResourceRelations.toArray());
    }
}