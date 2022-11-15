package org.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.util.CommonUtils;
import org.example.user.entity.Role;
import org.example.user.entity.RoleMenuRelation;
import org.example.user.entity.UserRoleRelation;
import org.example.user.entity.vo.RoleMenuRelationVo;
import org.example.user.entity.vo.RoleVo;
import org.example.user.mapper.RoleMapper;
import org.example.user.mapper.RoleMenuRelationMapper;
import org.example.user.mapper.UserRoleRelationMapper;
import org.example.user.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public Boolean addRole(RoleVo roleVo) {
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        role.setId(CommonUtils.uuid());
        roleMapper.insert(role);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteRole(String id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        roleMapper.deleteById(id);
        QueryWrapper<UserRoleRelation> userRoleRelationQueryWrapper = new QueryWrapper<>();
        userRoleRelationQueryWrapper.lambda().eq(UserRoleRelation::getRoleId, id);
        userRoleRelationMapper.delete(userRoleRelationQueryWrapper);
        QueryWrapper<RoleMenuRelation> roleMenuRelationQueryWrapper = new QueryWrapper<>();
        roleMenuRelationQueryWrapper.lambda().eq(RoleMenuRelation::getRoleId, id);
        roleMenuRelationMapper.delete(roleMenuRelationQueryWrapper);
        return true;
    }

    @Override
    public Boolean updateRole(RoleVo roleVo) {
        Role role = roleMapper.selectById(roleVo.getId());
        if (role == null) {
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        roleMapper.updateById(role);
        return true;
    }

    @Override
    public Boolean updateRoleMenu(RoleMenuRelationVo roleMenuRelationVo) {
        Role role = roleMapper.selectById(roleMenuRelationVo.getRoleId());
        if (role == null) {
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        QueryWrapper<RoleMenuRelation> roleMenuRelationQueryWrapper = new QueryWrapper<>();
        roleMenuRelationQueryWrapper.lambda().eq(RoleMenuRelation::getRoleId, roleMenuRelationVo.getRoleId());
        roleMenuRelationMapper.delete(roleMenuRelationQueryWrapper);
        List<String> menuIds = roleMenuRelationVo.getMenuIds();
        if (CollectionUtils.isEmpty(menuIds)) {
            menuIds.forEach(menuId -> {
                RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
                roleMenuRelation.setId(CommonUtils.uuid());
                roleMenuRelation.setRoleId(roleMenuRelationVo.getRoleId());
                roleMenuRelation.setMenuId(menuId);
                roleMenuRelationMapper.insert(roleMenuRelation);
            });
        }
        return true;
    }
}