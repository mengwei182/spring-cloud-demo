package org.example.system.service;

import org.example.system.entity.vo.RoleMenuRelationVo;
import org.example.system.entity.vo.RoleVo;

public interface RoleService {
    Boolean addRole(RoleVo roleVo);

    Boolean deleteRole(String id);

    Boolean updateRole(RoleVo roleVo);

    Boolean updateRoleMenu(RoleMenuRelationVo roleMenuRelationVo);
}