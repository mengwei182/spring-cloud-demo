package org.example.user.service;

import org.example.user.entity.vo.RoleMenuRelationVO;
import org.example.user.entity.vo.RoleVO;

public interface RoleService {
    Boolean addRole(RoleVO roleVo);

    Boolean deleteRole(String id);

    Boolean updateRole(RoleVO roleVo);

    Boolean updateRoleMenu(RoleMenuRelationVO roleMenuRelationVo);
}