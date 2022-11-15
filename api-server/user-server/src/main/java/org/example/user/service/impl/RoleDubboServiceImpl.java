package org.example.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.RoleDubboService;
import org.example.dubbo.userserver.entity.RoleDubboVO;
import org.example.user.entity.Role;
import org.example.user.service.cache.RoleCacheService;
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
    public List<RoleDubboVO> getRoleByUserId(String userId) {
        List<RoleDubboVO> roleDubboVOS = new ArrayList<>();
        List<Role> roleByUserId = roleCacheService.getRoleByUserId(userId);
        for (Role role : roleByUserId) {
            RoleDubboVO roleDubboVO = new RoleDubboVO();
            BeanUtils.copyProperties(role, roleDubboVO);
            roleDubboVOS.add(roleDubboVO);
        }
        return roleDubboVOS;
    }
}