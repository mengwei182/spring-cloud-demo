package org.example.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.dubbo.userserver.entity.Role;
import org.example.user.service.cache.RoleCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
@Service
@DubboService(interfaceClass = RoleDubboService.class, timeout = 10000)
public class RoleDubboServiceImpl implements RoleDubboService {
    @javax.annotation.Resource
    private RoleCacheService roleCacheService;

    @Override
    public List<Role> getRoleByUserId(String userId) {
        List<Role> resultRoles = new ArrayList<>();
        List<org.example.user.entity.Role> roleByUserId = roleCacheService.getRoleByUserId(userId);
        for (org.example.user.entity.Role role : roleByUserId) {
            Role resultRole = new Role();
            BeanUtils.copyProperties(role, resultRole);
            resultRoles.add(resultRole);
        }
        return resultRoles;
    }
}