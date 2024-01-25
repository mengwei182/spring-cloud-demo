package org.example.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.CaffeineRedisCache;
import org.example.common.entity.system.*;
import org.example.common.entity.system.vo.*;
import org.example.common.result.SystemServerResult;
import org.example.common.result.exception.SystemException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.common.util.RSAEncryptUtils;
import org.example.common.util.tree.TreeModelUtils;
import org.example.system.api.UserQueryPage;
import org.example.system.mapper.*;
import org.example.system.service.UserService;
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
public class UserServiceImpl implements UserService {
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

    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    @Override
    @Transactional
    public String addUser(UserVo userVo) {
        String username = userVo.getUsername();
        String password = userVo.getPassword();
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
        BeanUtils.copyProperties(userVo, user);
        String id = CommonUtils.uuid();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        String tokenExpireTime = userVo.getTokenExpireTime();
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
        List<String> roleIds = userVo.getRoleIds();
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
    public UserVo getUserInfo(String id) {
        if (StrUtil.isEmpty(id)) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        UserVo userVo = caffeineRedisCache.get(id, UserVo.class);
        if (userVo == null) {
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
            userVo = CommonUtils.transformObject(user, UserVo.class);
            userVo.setPassword(null);
            // 查询并填充用户角色信息
            List<String> roleIds = userRoleRelationMapper.selectList(new LambdaQueryWrapper<UserRoleRelation>().eq(UserRoleRelation::getUserId, user.getId())).stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            userVo.setRoles(CommonUtils.transformList(roles, RoleVo.class));
            // 查询并填充用户菜单信息
            List<String> menuIds = roleMenuRelationMapper.selectList(new LambdaQueryWrapper<RoleMenuRelation>().in(RoleMenuRelation::getRoleId, roleIds)).stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
            List<Menu> menus = menuMapper.selectBatchIds(menuIds);
            userVo.setMenus(TreeModelUtils.buildObjectTree(CommonUtils.transformList(menus, MenuVo.class)));
            // 查询并填充用户资源信息
            List<String> resourceIds = roleResourceRelationMapper.selectList(new LambdaQueryWrapper<RoleResourceRelation>().in(RoleResourceRelation::getRoleId, roleIds)).stream().map(RoleResourceRelation::getResourceId).collect(Collectors.toList());
            List<org.example.common.entity.system.Resource> resources = resourceMapper.selectBatchIds(resourceIds);
            userVo.setResources(CommonUtils.transformList(resources, ResourceVo.class));
            // 查询并填充用户部门信息
            Department department = departmentMapper.selectById(user.getDepartmentId());
            userVo.setDepartment(CommonUtils.transformObject(department, DepartmentVo.class));
            caffeineRedisCache.put(id, userVo, Duration.ofMillis(time));
        }
        return userVo;
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<UserVo> getUserList(UserQueryPage queryPage) {
        Page<User> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<User> userList = userMapper.getUserList(page, queryPage);
        page.setRecords(userList);
        return PageUtils.wrap(page, UserVo.class);
    }

    /**
     * 更新用户信息
     *
     * @param userVo
     * @return
     */
    @Override
    public Boolean updateUser(UserVo userVo) {
        User user = userMapper.selectById(userVo.getId());
        if (user == null) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userVo, user);
        userMapper.updateById(user);
        return true;
    }

    /**
     * 更新密码
     *
     * @param usernamePasswordVo
     * @return
     */
    @Override
    public Boolean updateUserPassword(UsernamePasswordVo usernamePasswordVo) {
        User user = userMapper.selectById(usernamePasswordVo.getId());
        if (user == null) {
            throw new SystemException(SystemServerResult.USER_NOT_EXIST);
        }
        String password = user.getPassword();
        String oldPassword = usernamePasswordVo.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new SystemException(SystemServerResult.OLD_PASSWORD_ERROR);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getPassword, passwordEncoder.encode(usernamePasswordVo.getPassword())).eq(User::getId, user.getId());
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
     * 获取用户token有效时间
     *
     * @param id
     * @return 有效时间，单位毫秒
     */
    @Override
    public long getTokenExpireTime(String id) {
        UserVo userVo = caffeineRedisCache.get(id, UserVo.class);
        if (userVo == null) {
            User user = userMapper.selectById(id);
            if (user == null) {
                return 0;
            }
            Date tokenExpireTime = user.getTokenExpireTime();
            if (tokenExpireTime == null) {
                // 默认7天有效期
                tokenExpireTime = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
                user.setTokenExpireTime(tokenExpireTime);
                userMapper.updateById(user);
                clear(id);
            }
            // 校验token有效期
            long time = tokenExpireTime.getTime() - new Date().getTime();
            return time <= 0 ? 0 : time;
        }
        return 0;
    }

    /**
     * 创建密钥
     *
     * @param id
     * @return
     */
    @Override
    public String createPublicKey(String id) throws NoSuchAlgorithmException {
        User user = new User();
        user.setId(id);
        user.setPublicKey(RSAEncryptUtils.getPublicKey());
        user.setPrivateKey(RSAEncryptUtils.getPublicKey());
        userMapper.updateById(user);
        clear(id);
        return RSAEncryptUtils.getPublicKey();
    }

    @Override
    public void clear(Object... ids) {
        caffeineRedisCache.evict(ids[0]);
    }
}