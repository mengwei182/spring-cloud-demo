package org.example.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.RoleResourceRelationDubboService;
import org.example.dubbo.userserver.entity.RoleResourceRelationDubboVO;
import org.example.user.entity.RoleResourceRelation;
import org.example.user.service.cache.RoleResourceRelationCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/14
 */
@Service
@DubboService(interfaceClass = RoleResourceRelationDubboService.class, timeout = 10000)
public class RoleResourceRelationDubboServiceImpl implements RoleResourceRelationDubboService {
    @Resource
    private RoleResourceRelationCacheService roleResourceRelationCacheService;

    @Override
    public List<RoleResourceRelationDubboVO> getRoleResourceRelations() {
        List<RoleResourceRelationDubboVO> roleResourceRelationDubboVOS = new ArrayList<>();
        List<RoleResourceRelation> roleResourceRelations = roleResourceRelationCacheService.getRoleResourceRelations();
        for (RoleResourceRelation roleResourceRelation : roleResourceRelations) {
            RoleResourceRelationDubboVO roleResourceRelationDubboVO = new RoleResourceRelationDubboVO();
            BeanUtils.copyProperties(roleResourceRelation, roleResourceRelationDubboVO);
            roleResourceRelationDubboVOS.add(roleResourceRelationDubboVO);
        }
        return roleResourceRelationDubboVOS;
    }
}