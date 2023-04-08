package org.example.system.service.cache.impl;

import org.example.common.entity.system.Role;
import org.example.common.util.CommonUtils;
import org.example.system.mapper.RoleMapper;
import org.example.system.service.cache.RoleCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lihui
 * @since 2022/10/30
 */
@Service
public class RoleCacheServiceImpl implements RoleCacheService {
    private static final String ROLE_CACHE_PREFIX = "ROLE_CACHE_PREFIX_";
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleByUserId(String userId) {
        List<Role> roles = new ArrayList<>();
        Set<Object> members = redisTemplate.opsForSet().members(ROLE_CACHE_PREFIX + userId);
        if (!CollectionUtils.isEmpty(members)) {
            for (Object member : members) {
                roles.add(CommonUtils.gson().fromJson(CommonUtils.gson().toJson(member), Role.class));
            }
        } else {
            roles = roleMapper.selectList(null);
            redisTemplate.opsForSet().add(ROLE_CACHE_PREFIX + userId, roles.toArray());
        }
        return roles;
    }

    @Override
    public void setRoleByUserId(String userId, List<Role> roles) {
        redisTemplate.opsForSet().add(ROLE_CACHE_PREFIX + userId, roles.toArray());
    }
}