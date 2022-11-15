package org.example.user.controller;

import org.example.common.model.CommonResult;
import org.example.user.entity.vo.RoleMenuRelationVO;
import org.example.user.entity.vo.RoleVO;
import org.example.user.service.RoleService;
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

    @RequestMapping("/add")
    public CommonResult addRole(@RequestBody RoleVO roleVo) {
        return CommonResult.success(roleService.addRole(roleVo));
    }

    @RequestMapping("/delete")
    public CommonResult deleteRole(@RequestParam String id) {
        return CommonResult.success(roleService.deleteRole(id));
    }

    @RequestMapping("/update")
    public CommonResult updateRole(@RequestBody RoleVO roleVo) {
        return CommonResult.success(roleService.updateRole(roleVo));
    }

    @RequestMapping("/menu/update")
    public CommonResult updateRoleMenu(@RequestBody RoleMenuRelationVO roleMenuRelationVo) {
        return CommonResult.success(roleService.updateRoleMenu(roleMenuRelationVo));
    }
}