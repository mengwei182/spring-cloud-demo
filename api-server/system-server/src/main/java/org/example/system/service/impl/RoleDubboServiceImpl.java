package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.dubbo.userserver.entity.RoleDubboVo;
import org.example.system.entity.Role;
import org.example.system.service.cache.RoleCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
@Service
@DubboService(interfaceClass = RoleDubboService.class, timeout = 10000)
public class RoleDubboServiceImpl implements RoleDubboService {
    @Resource
    private RoleCacheService roleCacheService;

    @Override
    public List<RoleDubboVo> getRoleByUserId(String userId) {
        List<RoleDubboVo> roleDubboVos = new ArrayList<>();
        List<Role> roleByUserId = roleCacheService.getRoleByUserId(userId);
        for (Role role : roleByUserId) {
            RoleDubboVo roleDubboVO = new RoleDubboVo();
            BeanUtils.copyProperties(role, roleDubboVO);
            roleDubboVos.add(roleDubboVO);
        }
        return roleDubboVos;
    }
}