package org.example.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.RoleResourceRelationDubboService;
import org.example.dubbo.userserver.entity.RoleResourceRelation;
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
    public List<RoleResourceRelation> getRoleResourceRelations() {
        List<RoleResourceRelation> resultRoleResourceRelations = new ArrayList<>();
        List<org.example.user.entity.RoleResourceRelation> roleResourceRelations = roleResourceRelationCacheService.getRoleResourceRelations();
        for (org.example.user.entity.RoleResourceRelation roleResourceRelation : roleResourceRelations) {
            RoleResourceRelation resultRoleResourceRelation = new RoleResourceRelation();
            BeanUtils.copyProperties(roleResourceRelation, resultRoleResourceRelation);
            resultRoleResourceRelations.add(resultRoleResourceRelation);
        }
        return resultRoleResourceRelations;
    }
}