package org.example.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.result.CommonServerResult;
import org.example.common.result.SystemServerResult;
import org.example.common.result.exception.SystemException;
import org.example.system.dubbo.RoleDubboService;
import org.example.system.entity.Role;
import org.example.system.entity.RoleMenuRelation;
import org.example.system.entity.UserRoleRelation;
import org.example.system.mapper.RoleMapper;
import org.example.system.mapper.RoleMenuRelationMapper;
import org.example.system.mapper.UserRoleRelationMapper;
import org.example.system.query.RoleQueryPage;
import org.example.system.service.RoleService;
import org.example.system.vo.RoleMenuRelationVO;
import org.example.system.vo.RoleVO;
import org.example.util.CommonUtils;
import org.example.util.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = RoleDubboService.class, interfaceName = "roleDubboService")
public class RoleServiceImpl implements RoleService, RoleDubboService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;

    /**
     * 新增角色
     *
     * @param roleVO
     * @return
     */
    @Override
    public Boolean addRole(RoleVO roleVO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleVO, role);
        role.setId(CommonUtils.uuid());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        Role resultRole = roleMapper.selectOne(queryWrapper.eq(Role::getName, roleVO.getName()));
        if (resultRole != null) {
            throw new SystemException(SystemServerResult.ROLE_NAME_EXIST);
        }
        roleMapper.insert(role);
        return true;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteRole(String id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new SystemException(CommonServerResult.OBJECT_NOT_EXIST);
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

    /**
     * 更新角色
     *
     * @param roleVO
     * @return
     */
    @Override
    public Boolean updateRole(RoleVO roleVO) {
        Role role = roleMapper.selectById(roleVO.getId());
        if (role == null) {
            throw new SystemException(CommonServerResult.OBJECT_NOT_EXIST);
        }
        role = new Role();
        BeanUtils.copyProperties(roleVO, role);
        roleMapper.updateById(role);
        return true;
    }

    /**
     * 新增角色菜单关系
     *
     * @param roleMenuRelationVO
     * @return
     */
    @Override
    @Transactional
    public Boolean addRoleMenu(RoleMenuRelationVO roleMenuRelationVO) {
        Role role = roleMapper.selectById(roleMenuRelationVO.getRoleId());
        if (role == null) {
            throw new SystemException(CommonServerResult.OBJECT_NOT_EXIST);
        }
        QueryWrapper<RoleMenuRelation> roleMenuRelationQueryWrapper = new QueryWrapper<>();
        roleMenuRelationQueryWrapper.lambda().eq(RoleMenuRelation::getRoleId, roleMenuRelationVO.getRoleId());
        roleMenuRelationMapper.delete(roleMenuRelationQueryWrapper);
        List<String> menuIds = roleMenuRelationVO.getMenuIds();
        if (!CollectionUtil.isEmpty(menuIds)) {
            menuIds.forEach(menuId -> {
                RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
                roleMenuRelation.setId(CommonUtils.uuid());
                roleMenuRelation.setRoleId(roleMenuRelationVO.getRoleId());
                roleMenuRelation.setMenuId(menuId);
                roleMenuRelationMapper.insert(roleMenuRelation);
            });
        }
        return true;
    }

    /**
     * 查询角色列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<RoleVO> getRoleList(RoleQueryPage queryPage) {
        Page<Role> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<Role> roleList = roleMapper.getRoleList(page, queryPage);
        page.setRecords(roleList);
        return PageUtils.wrap(page, RoleVO.class);
    }

    /**
     * 查询所有角色列表
     *
     * @return
     */
    @Override
    public List<RoleVO> getAllRoleList() {
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<>());
        return CommonUtils.transformList(roles, RoleVO.class);
    }
}