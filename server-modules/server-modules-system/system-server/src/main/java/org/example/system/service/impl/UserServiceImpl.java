package org.example.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.CaffeineRedisCache;
import org.example.common.core.domain.LoginUser;
import org.example.common.core.exception.ExceptionInformation;
import org.example.common.core.usercontext.UserContext;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.PageUtils;
import org.example.common.core.util.RSAEncryptUtils;
import org.example.common.core.util.TreeModelUtils;
import org.example.system.dubbo.UserDubboService;
import org.example.system.entity.Department;
import org.example.system.entity.Menu;
import org.example.system.entity.Role;
import org.example.system.entity.RoleMenuRelation;
import org.example.system.entity.RoleResourceRelation;
import org.example.system.entity.User;
import org.example.system.entity.UserDepartmentRelation;
import org.example.system.entity.UserRoleRelation;
import org.example.system.entity.vo.DepartmentVO;
import org.example.system.entity.vo.MenuVO;
import org.example.system.entity.vo.RoleVO;
import org.example.system.entity.vo.UserLoginVO;
import org.example.system.entity.vo.UserVO;
import org.example.system.exception.SystemException;
import org.example.system.mapper.DepartmentMapper;
import org.example.system.mapper.MenuMapper;
import org.example.system.mapper.ResourceMapper;
import org.example.system.mapper.RoleMapper;
import org.example.system.mapper.RoleMenuRelationMapper;
import org.example.system.mapper.RoleResourceRelationMapper;
import org.example.system.mapper.UserDepartmentRelationMapper;
import org.example.system.mapper.UserMapper;
import org.example.system.mapper.UserRoleRelationMapper;
import org.example.system.query.UserQueryPage;
import org.example.system.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
@Service
@DubboService(interfaceClass = UserDubboService.class)
public class UserServiceImpl implements UserService, UserDubboService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private CaffeineRedisCache caffeineRedisCache;
    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private RoleResourceRelationMapper roleResourceRelationMapper;
    @Resource
    private UserDepartmentRelationMapper userDepartmentRelationMapper;

    /**
     * 新增用户
     *
     * @param userVO
     * @return
     */
    @Override
    @Transactional
    public String addUser(UserVO userVO) {
        String username = userVO.getUsername();
        String password = userVO.getPassword();
        if (StrUtil.isEmpty(username)) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2007.getCode(), ExceptionInformation.AUTHENTICATION_2007.getMessage());
        }
        if (StrUtil.isEmpty(password)) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2008.getCode(), ExceptionInformation.AUTHENTICATION_2008.getMessage());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2010.getCode(), ExceptionInformation.AUTHENTICATION_2010.getMessage());
        }
        user = new User();
        BeanUtils.copyProperties(userVO, user);
        String id = CommonUtils.uuid();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        String tokenExpireTime = userVO.getTokenExpireTime();
        if (StrUtil.isEmpty(tokenExpireTime)) {
            // 默认7天有效期
            user.setTokenExpireTime(Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            user.setTokenExpireTime(Date.from(LocalDateTime.parse(tokenExpireTime).atZone(ZoneId.systemDefault()).toInstant()));
        }
        user.setCreator(UserContext.get().getId());
        user.setUpdater(UserContext.get().getId());
        userMapper.insert(user);
        // 添加角色信息
        List<String> roleIds = userVO.getRoleIds();
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new SystemException(ExceptionInformation.SYSTEM_3016.getCode(), ExceptionInformation.SYSTEM_3016.getMessage());
        }
        for (String roleId : roleIds) {
            if (Role.ADMIN_ID.equals(roleId)) {
                throw new SystemException(ExceptionInformation.SYSTEM_3017.getCode(), ExceptionInformation.SYSTEM_3017.getMessage());
            }
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserId(user.getId());
            userRoleRelation.setRoleId(roleId);
            userRoleRelation.setCreator(UserContext.get().getId());
            userRoleRelation.setUpdater(UserContext.get().getId());
            userRoleRelationMapper.insert(userRoleRelation);
        }
        return id;
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @Override
    public UserVO getUserInfo(String id) {
        if (StrUtil.isEmpty(id)) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2011.getCode(), ExceptionInformation.AUTHENTICATION_2011.getMessage());
        }
        UserVO userVO;
        LoginUser loginUser = caffeineRedisCache.get(id, LoginUser.class);
        if (loginUser == null) {
            User user = userMapper.selectById(id);
            if (user == null) {
                return null;
            }
            userVO = CommonUtils.transformObject(user, UserVO.class);
        } else {
            userVO = CommonUtils.transformObject(loginUser, UserVO.class);
        }
        loadUserInfo(userVO);
        return userVO;
    }

    private void loadUserInfo(UserVO userVO) {
        // 查询并填充用户角色信息
        List<String> roleIds = userRoleRelationMapper.selectList(new LambdaQueryWrapper<UserRoleRelation>().eq(UserRoleRelation::getUserId, userVO.getId())).stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            userVO.setRoles(CommonUtils.transformList(roles, RoleVO.class));
        }
        // 查询并填充用户菜单信息
        List<String> menuIds = roleMenuRelationMapper.selectList(new LambdaQueryWrapper<RoleMenuRelation>().in(RoleMenuRelation::getRoleId, roleIds)).stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<Menu> menus = menuMapper.selectBatchIds(menuIds);
            userVO.setMenus(TreeModelUtils.buildObjectTree(CommonUtils.transformList(menus, MenuVO.class)));
        }
        // 查询并填充用户资源信息
        List<String> resourceIds = roleResourceRelationMapper.selectList(new LambdaQueryWrapper<RoleResourceRelation>().in(RoleResourceRelation::getRoleId, roleIds)).stream().map(RoleResourceRelation::getResourceId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(resourceIds)) {
            List<org.example.system.entity.Resource> resources = resourceMapper.selectBatchIds(resourceIds);
            userVO.setResourceUrls(resources.stream().map(org.example.system.entity.Resource::getUrl).collect(Collectors.toList()));
        }
        // 查询并填充用户部门信息
        List<String> departmentIds = userDepartmentRelationMapper.selectList(new LambdaQueryWrapper<UserDepartmentRelation>().in(UserDepartmentRelation::getUserId, userVO.getId())).stream().map(UserDepartmentRelation::getDepartmentId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(departmentIds)) {
            List<Department> departments = departmentMapper.selectBatchIds(departmentIds);
            userVO.setDepartments(TreeModelUtils.buildObjectTree(CommonUtils.transformList(departments, DepartmentVO.class)));
        }
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<UserVO> getUserList(UserQueryPage queryPage) {
        Page<User> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<User> userList = userMapper.getUserList(page, queryPage);
        page.setRecords(userList);
        return PageUtils.wrap(page, UserVO.class);
    }

    /**
     * 更新用户信息
     *
     * @param userVO
     * @return
     */
    @Override
    public Boolean updateUser(UserVO userVO) {
        User user = userMapper.selectById(userVO.getId());
        if (user == null) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2011.getCode(), ExceptionInformation.AUTHENTICATION_2011.getMessage());
        }
        BeanUtils.copyProperties(userVO, user);
        userMapper.updateById(user);
        return true;
    }

    /**
     * 更新密码
     *
     * @param userLoginVo
     * @return
     */
    @Override
    public Boolean updateUserPassword(UserLoginVO userLoginVo) {
        User user = userMapper.selectById(userLoginVo.getId());
        if (user == null) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2011.getCode(), ExceptionInformation.AUTHENTICATION_2011.getMessage());
        }
        String password = user.getPassword();
        String oldPassword = userLoginVo.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new SystemException(ExceptionInformation.AUTHENTICATION_2013.getCode(), ExceptionInformation.AUTHENTICATION_2013.getMessage());
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getPassword, passwordEncoder.encode(userLoginVo.getPassword())).eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteUser(String id) {
        userMapper.deleteById(id);
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleRelation::getUserId, id);
        userRoleRelationMapper.delete(queryWrapper);
        return true;
    }

    /**
     * 根据user id获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public User getUser(String userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 根据username获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    /**
     * 清理用户信息缓存
     *
     * @param userId
     */
    @Override
    public void clearUserCache(String userId) {
        caffeineRedisCache.evict(userId);
    }

    /**
     * 获取密钥
     *
     * @param id
     * @return
     */
    @Override
    public String getPublicKey(String id) throws NoSuchAlgorithmException {
        User user = new User();
        user.setId(id);
        user.setPublicKey(RSAEncryptUtils.getPublicKey());
        user.setPrivateKey(RSAEncryptUtils.getPublicKey());
        userMapper.updateById(user);
        clearUserCache(id);
        return RSAEncryptUtils.getPublicKey();
    }
}