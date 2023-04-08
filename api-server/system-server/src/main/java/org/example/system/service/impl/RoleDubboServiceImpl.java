package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.entity.system.Role;
import org.example.common.entity.system.vo.RoleVo;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.system.service.cache.RoleCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
@Service
@DubboService(interfaceClass = RoleDubboService.class, timeout = 10000)
public class RoleDubboServiceImpl implements RoleDubboService {
    @Resource
    private RoleCacheService roleCacheService;

    @Override
    public List<RoleVo> getRoleByUserId(String userId) {
        List<RoleVo> roleVos = new ArrayList<>();
        List<Role> roleByUserId = roleCacheService.getRoleByUserId(userId);
        for (Role role : roleByUserId) {
            RoleVo roleVO = new RoleVo();
            BeanUtils.copyProperties(role, roleVO);
            roleVos.add(roleVO);
        }
        return roleVos;
    }
}