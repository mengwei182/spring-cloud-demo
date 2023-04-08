package org.example.system.service.cache;

import org.example.common.entity.system.RoleResourceRelation;

import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
public interface RoleResourceRelationCacheService {
    List<RoleResourceRelation> getRoleResourceRelations();

    void setRoleResourceRelations(List<RoleResourceRelation> roleResourceRelations);
}