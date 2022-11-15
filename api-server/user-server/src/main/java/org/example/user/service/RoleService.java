package org.example.user.service;

import org.example.user.entity.vo.RoleMenuRelationVo;
import org.example.user.entity.vo.RoleVo;

public interface RoleService {
    Boolean addRole(RoleVo roleVo);

    Boolean deleteRole(String id);

    Boolean updateRole(RoleVo roleVo);

    Boolean updateRoleMenu(RoleMenuRelationVo roleMenuRelationVo);
}