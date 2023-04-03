package org.example.system.service.cache;

import org.example.system.entity.RoleResourceRelation;

import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
public interface RoleResourceRelationCacheService {
    List<RoleResourceRelation> getRoleResourceRelations();

    void setRoleResourceRelations(List<RoleResourceRelation> roleResourceRelations);
}