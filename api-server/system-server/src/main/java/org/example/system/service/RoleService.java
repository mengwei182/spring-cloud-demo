package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.system.vo.RoleMenuRelationVo;
import org.example.common.entity.system.vo.RoleVo;
import org.example.system.api.RoleQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
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

    /**
     * 查询角色列表
     *
     * @param queryPage
     * @return
     */
    Page<RoleVo> getRoleList(RoleQueryPage queryPage);

    /**
     * 查询所有角色列表
     *
     * @return
     */
    List<RoleVo> getAllRoleList();
}