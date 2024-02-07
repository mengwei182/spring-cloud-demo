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
import org.example.common.result.SystemServerResult;
import org.example.common.result.exception.SystemException;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.common.util.RSAEncryptUtils;
import org.example.common.util.TreeModelUtils;
import org.example.system.dubbo.UserDubboService;
import org.example.system.entity.*;
import org.example.system.entity.vo.*;
import org.example.system.mapper.*;
import org.example.system.query.UserQueryPage;
import org.example.system.service.UserService;
import org.example.usercontext.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
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
            throw new SystemException(SystemServerResult.USERNAME_NULL);
        }
        if (StrUtil.isEmpty(password)) {
            throw new SystemException(SystemServerResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new SystemException(SystemServerResult.USER_EXIST);
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
            throw new SystemException(SystemServerResult.ROLE_NULL);
        }
        for (String roleId : roleIds) {
            if (Role.ADMIN_ID.equals(roleId)) {
                throw new SystemException(SystemServerResult.ROLE_ADMIN_EXIST);
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
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        UserVO userVO = caffeineRedisCache.get(id, UserVO.class);
        if (userVO == null) {
            User user = userMapper.selectById(id);
            if (user == null) {
                return null;
            }
            // 校验token有效期
            long time = getTokenExpireTime(user.getId());
            // token已经过期
            if (time <= 0) {
                // 删除旧token
                caffeineRedisCache.evict(SystemServerResult.USER_TOKEN_KEY + user.getId());
                throw new SystemException(SystemServerResult.TOKEN_EXPIRATION_TIME_INVALID);
            }
            userVO = CommonUtils.transformObject(user, UserVO.class);
            userVO.setPassword(null);
            loadUserInfo(userVO);
            caffeineRedisCache.put(id, userVO, Duration.ofMillis(time));
        }
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
            userVO.setResources(CommonUtils.transformList(resources, ResourceVO.class));
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
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
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
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        String password = user.getPassword();
        String oldPassword = userLoginVo.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new SystemException(SystemServerResult.OLD_PASSWORD_ERROR);
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
     * 获取用户token有效时间
     *
     * @param id
     * @return 有效时间，单位毫秒
     */
    @Override
    public long getTokenExpireTime(String id) {
        User user = null;
        UserVO userVO = caffeineRedisCache.get(id, UserVO.class);
        if (userVO == null) {
            user = userMapper.selectById(id);
        }
        if (user == null) {
            return 0;
        }
        Date tokenExpireTime = user.getTokenExpireTime();
        if (tokenExpireTime == null) {
            // 默认7天有效期
            tokenExpireTime = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
            user.setTokenExpireTime(tokenExpireTime);
            userMapper.updateById(user);
            clearUserCache(id);
        }
        // 校验token有效期
        long time = tokenExpireTime.getTime() - new Date().getTime();
        return time <= 0 ? 0 : time;
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