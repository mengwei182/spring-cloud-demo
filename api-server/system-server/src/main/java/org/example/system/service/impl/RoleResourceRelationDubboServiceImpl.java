package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.entity.system.RoleResourceRelation;
import org.example.common.entity.system.vo.RoleResourceRelationVo;
import org.example.dubbo.userserver.RoleResourceRelationDubboService;
import org.example.system.service.cache.RoleResourceRelationCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihui
 * @since 2022/11/14
 */
@Service
@DubboService(interfaceClass = RoleResourceRelationDubboService.class, timeout = 10000)
public class RoleResourceRelationDubboServiceImpl implements RoleResourceRelationDubboService {
    @Resource
    private RoleResourceRelationCacheService roleResourceRelationCacheService;

    @Override
    public List<RoleResourceRelationVo> getRoleResourceRelations() {
        List<RoleResourceRelationVo> roleResourceRelationVos = new ArrayList<>();
        List<RoleResourceRelation> roleResourceRelations = roleResourceRelationCacheService.getRoleResourceRelations();
        for (RoleResourceRelation roleResourceRelation : roleResourceRelations) {
            RoleResourceRelationVo roleResourceRelationVO = new RoleResourceRelationVo();
            BeanUtils.copyProperties(roleResourceRelation, roleResourceRelationVO);
            roleResourceRelationVos.add(roleResourceRelationVO);
        }
        return roleResourceRelationVos;
    }
}