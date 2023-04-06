package org.example.system.service;

import org.example.system.entity.vo.RoleMenuRelationVo;
import org.example.system.entity.vo.RoleVo;

public interface RoleService {
    /**
     * 新增角色
     *
     * @param roleVo
     * @return
     */
    Boolean addRole(RoleVo roleVo);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    Boolean deleteRole(String id);

    /**
     * 更新角色
     *
     * @param roleVo
     * @return
     */
    Boolean updateRole(RoleVo roleVo);

    /**
     * 新增角色菜单关系
     *
     * @param roleMenuRelationVo
     * @return
     */
    Boolean addRoleMenu(RoleMenuRelationVo roleMenuRelationVo);
}