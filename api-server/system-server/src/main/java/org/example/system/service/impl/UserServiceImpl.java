package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.*;
import org.example.common.entity.system.vo.*;
import org.example.common.error.SystemServerResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.common.util.tree.TreeModelUtils;
import org.example.system.api.UserQueryPage;
import org.example.system.mapper.*;
import org.example.system.service.UserService;
import org.example.system.service.cache.UserCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService, UserCacheService {
    private static final String PHONE_VERIFY_PREFIX = "PHONE_VERIFY_PREFIX_";
    private static final String IMAGE_VERIFY_PREFIX = "IMAGE_VERIFY_PREFIX_";
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
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private RoleResourceRelationMapper roleResourceRelationMapper;

    /**
     * 新增用户
     *
     * @param userInfoVo
     * @return
     */
    @Override
    @Transactional
    public Boolean addUser(UserInfoVo userInfoVo) {
        String username = userInfoVo.getUsername();
        String password = userInfoVo.getPassword();
        if (!StringUtils.hasLength(username)) {
            throw new CommonException(SystemServerResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(SystemServerResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new CommonException(SystemServerResult.USER_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(userInfoVo, user);
        user.setId(CommonUtils.uuid());
        user.setPassword(passwordEncoder.encode(password));
        user.setCreator(UserContext.get().getUserId());
        user.setUpdater(UserContext.get().getUserId());
        userMapper.insert(user);
        // 添加角色信息
        List<String> roleIds = userInfoVo.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                UserRoleRelation userRoleRelation = new UserRoleRelation();
                userRoleRelation.setUserId(user.getId());
                userRoleRelation.setRoleId(roleId);
                userRoleRelation.setCreator(UserContext.get().getUserId());
                userRoleRelation.setUpdater(UserContext.get().getUserId());
                userRoleRelationMapper.insert(userRoleRelation);
            }
        }
        return true;
    }

    /**
     * 查看用户详情
     *
     * @return
     */
    @Override
    public UserInfoVo getUserInfo(String id) {
        if (!StringUtils.hasLength(id)) {
            throw new CommonException(SystemServerResult.USER_NOT_EXIST);
        }
        User user = userMapper.selectById(id);
        UserInfoVo userInfoVo = CommonUtils.transformObject(user, UserInfoVo.class);
        userInfoVo.setPassword(null);
        // 查询并填充用户角色信息
        List<String> roleIds = userRoleRelationMapper.selectList(new LambdaQueryWrapper<UserRoleRelation>().eq(UserRoleRelation::getUserId, user.getId())).stream().map(UserRoleRelation::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        userInfoVo.setRoles(CommonUtils.transformList(roles, RoleVo.class));
        // 查询并填充用户菜单信息
        List<String> menuIds = roleMenuRelationMapper.selectList(new LambdaQueryWrapper<RoleMenuRelation>().in(RoleMenuRelation::getRoleId, roleIds)).stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
        List<Menu> menus = menuMapper.selectBatchIds(menuIds);
        userInfoVo.setMenus(TreeModelUtils.buildObjectTree(CommonUtils.transformList(menus, MenuVo.class)));
        // 查询并填充用户资源信息
        List<String> resourceIds = roleResourceRelationMapper.selectList(new LambdaQueryWrapper<RoleResourceRelation>().in(RoleResourceRelation::getRoleId, roleIds)).stream().map(RoleResourceRelation::getResourceId).collect(Collectors.toList());
        List<org.example.common.entity.system.Resource> resources = resourceMapper.selectBatchIds(resourceIds);
        userInfoVo.setResources(CommonUtils.transformList(resources, ResourceVo.class));
        // 查询并填充用户部门信息
        Department department = departmentMapper.selectById(user.getDepartmentId());
        userInfoVo.setDepartment(CommonUtils.transformObject(department, DepartmentVo.class));
        return userInfoVo;
    }

    /**
     * 分页查看用户列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<UserInfoVo> getUserList(UserQueryPage queryPage) {
        Page<User> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<User> userList = userMapper.getUserList(page, queryPage);
        page.setRecords(userList);
        return PageUtils.wrap(page, UserInfoVo.class);
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVo
     * @return
     */
    @Override
    public Boolean updateUser(UserInfoVo userInfoVo) {
        User user = userMapper.selectById(userInfoVo.getId());
        if (user == null) {
            throw new CommonException(SystemServerResult.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userInfoVo, user);
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
            throw new CommonException(SystemServerResult.USER_NOT_EXIST);
        }
        String phone = usernamePasswordVo.getPhone();
        String phoneVerifyCode = usernamePasswordVo.getPhoneVerifyCode();
        if (!StringUtils.hasLength(phoneVerifyCode)) {
            throw new CommonException(SystemServerResult.VERIFY_CODE_ERROR);
        }
        String phoneVerifyCodeCache = getPhoneVerifyCode(phone);
        if (!StringUtils.hasLength(phoneVerifyCodeCache)) {
            throw new CommonException(SystemServerResult.VERIFY_CODE_OVERDUE);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getPassword, passwordEncoder.encode(usernamePasswordVo.getPassword())).eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
        deletePhoneVerifyCode(phone);
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
     * 设置手机验证码到redis
     *
     * @param phone
     * @param verifyCode
     * @param timeout
     */
    @Override
    public void setPhoneVerifyCode(String phone, String verifyCode, Long timeout) {
        if (timeout != null && timeout > 0) {
            redisTemplate.opsForValue().set(PHONE_VERIFY_PREFIX.concat(phone), verifyCode, timeout, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(PHONE_VERIFY_PREFIX.concat(phone), verifyCode);
        }
    }

    /**
     * 从redis获取手机验证码
     *
     * @param phone
     * @return
     */
    @Override
    public String getPhoneVerifyCode(String phone) {
        return (String) redisTemplate.opsForValue().get(PHONE_VERIFY_PREFIX.concat(phone));
    }

    /**
     * 从redis删除手机验证码
     *
     * @param phone
     */
    @Override
    public void deletePhoneVerifyCode(String phone) {
        redisTemplate.delete(PHONE_VERIFY_PREFIX.concat(phone));
    }

    /**
     * 设置图片验证码到redis
     *
     * @param account
     * @param verifyCode
     * @param timeout
     */
    @Override
    public void setImageVerifyCode(String account, String verifyCode, Long timeout) {
        if (timeout != null && timeout > 0) {
            redisTemplate.opsForValue().set(IMAGE_VERIFY_PREFIX.concat(account), verifyCode, timeout, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(IMAGE_VERIFY_PREFIX.concat(account), verifyCode);
        }
    }

    /**
     * 从redis获取图片验证码
     *
     * @param account
     */
    @Override
    public String getImageVerifyCode(String account) {
        return (String) redisTemplate.opsForValue().get(IMAGE_VERIFY_PREFIX.concat(account));
    }

    /**
     * 从redis删除图片验证码
     *
     * @param account
     */
    @Override
    public void deleteImageVerifyCode(String account) {
        redisTemplate.delete(IMAGE_VERIFY_PREFIX.concat(account));
    }
}