package org.example.system.controller;

import org.example.common.model.CommonResult;
import org.example.system.entity.vo.RoleMenuRelationVo;
import org.example.system.entity.vo.RoleVo;
import org.example.system.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * 新增角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping("/add")
    public CommonResult addRole(@RequestBody RoleVo roleVo) {
        return CommonResult.success(roleService.addRole(roleVo));
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public CommonResult deleteRole(@RequestParam String id) {
        return CommonResult.success(roleService.deleteRole(id));
    }

    /**
     * 更新角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping("/update")
    public CommonResult updateRole(@RequestBody RoleVo roleVo) {
        return CommonResult.success(roleService.updateRole(roleVo));
    }

    /**
     * 新增角色菜单关系
     *
     * @param roleMenuRelationVo
     * @return
     */
    @RequestMapping("/menu/update")
    public CommonResult addRoleMenu(@RequestBody RoleMenuRelationVo roleMenuRelationVo) {
        return CommonResult.success(roleService.addRoleMenu(roleMenuRelationVo));
    }
}