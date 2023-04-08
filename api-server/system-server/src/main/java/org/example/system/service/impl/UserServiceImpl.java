package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.base.vo.UserInfoVo;
import org.example.common.entity.system.User;
import org.example.common.entity.system.UserRoleRelation;
import org.example.common.entity.system.vo.UsernamePasswordVo;
import org.example.common.error.SystemServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.system.api.UserQueryPage;
import org.example.system.mapper.UserMapper;
import org.example.system.mapper.UserRoleRelationMapper;
import org.example.system.service.UserService;
import org.example.system.service.cache.UserCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserCacheService userCacheService;

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
            throw new CommonException(SystemServerErrorResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(SystemServerErrorResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new CommonException(SystemServerErrorResult.USER_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(userInfoVo, user);
        user.setId(CommonUtils.uuid());
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateId(UserContext.get().getUserId());
        user.setUpdateId(UserContext.get().getUserId());
        userMapper.insert(user);
        // 添加角色信息
        List<String> roleIds = userInfoVo.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                UserRoleRelation userRoleRelation = new UserRoleRelation();
                userRoleRelation.setUserId(user.getId());
                userRoleRelation.setRoleId(roleId);
                userRoleRelation.setCreateId(UserContext.get().getUserId());
                userRoleRelation.setUpdateId(UserContext.get().getUserId());
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
            throw new CommonException(SystemServerErrorResult.USER_NOT_EXIST);
        }
        User user = userMapper.selectById(id);
        return CommonUtils.transformObject(user, UserInfoVo.class);
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
            throw new CommonException(SystemServerErrorResult.USER_NOT_EXIST);
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
            throw new CommonException(SystemServerErrorResult.USER_NOT_EXIST);
        }
        String phone = usernamePasswordVo.getPhone();
        String phoneVerifyCode = usernamePasswordVo.getPhoneVerifyCode();
        if (!StringUtils.hasLength(phoneVerifyCode)) {
            throw new CommonException(SystemServerErrorResult.VERIFY_CODE_ERROR);
        }
        String phoneVerifyCodeCache = userCacheService.getPhoneVerifyCode(phone);
        if (!StringUtils.hasLength(phoneVerifyCodeCache)) {
            throw new CommonException(SystemServerErrorResult.VERIFY_CODE_OVERDUE);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getPassword, passwordEncoder.encode(usernamePasswordVo.getPassword())).eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
        userCacheService.deletePhoneVerifyCode(phone);
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
}