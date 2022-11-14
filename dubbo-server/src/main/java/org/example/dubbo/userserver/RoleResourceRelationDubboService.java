package org.example.dubbo.userserver;

import org.example.dubbo.userserver.entity.RoleResourceRelation;

import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
public interface RoleResourceRelationDubboService {
    List<RoleResourceRelation> getRoleResourceRelations();
}