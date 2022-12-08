package org.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.vo.TokenVo;
import org.example.common.entity.vo.UserInfoVo;
import org.example.common.error.UserServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.usercontext.UserContext;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.common.util.TokenUtil;
import org.example.user.api.UserQueryPage;
import org.example.user.entity.User;
import org.example.user.entity.UserRoleRelation;
import org.example.user.entity.vo.UserRoleRelationVo;
import org.example.user.entity.vo.UsernamePasswordVo;
import org.example.user.mapper.*;
import org.example.user.service.UserService;
import org.example.user.service.cache.UserCacheService;
import org.example.user.util.ImageVerifyCodeUtils;
import org.example.user.util.MessageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;
    @Resource
    private RoleResourceRelationMapper roleResourceRelationMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserCacheService userCacheService;

    @Override
    public String login(UsernamePasswordVo usernamePasswordVo) {
        String username = usernamePasswordVo.getUsername();
        String password = usernamePasswordVo.getPassword();
        if (!StringUtils.hasLength(username)) {
            throw new CommonException(UserServerErrorResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(UserServerErrorResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new CommonException(UserServerErrorResult.USER_NOT_EXIST);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CommonException(UserServerErrorResult.PASSWORD_ERROR);
        }
        Date loginTime = new Date();
        UserInfoVo userInfoVo = buildUserVo(user);
        TokenVo<?> tokenVo = new TokenVo<>(user.getId(), loginTime, 60 * 60L, userInfoVo);
        String token = TokenUtil.sign(tokenVo);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getLoginTime, new Date()).eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
        return token;
    }

    @Override
    public Boolean logout() {
        UserContext.remove();
        return true;
    }

    @Override
    public Boolean register(UserInfoVo userInfoVo) {
        String username = userInfoVo.getUsername();
        String password = userInfoVo.getPassword();
        if (!StringUtils.hasLength(username)) {
            throw new CommonException(UserServerErrorResult.USERNAME_NULL);
        }
        if (!StringUtils.hasLength(password)) {
            throw new CommonException(UserServerErrorResult.PASSWORD_NULL);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new CommonException(UserServerErrorResult.USER_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(userInfoVo, user);
        user.setId(CommonUtils.uuid());
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateId("");
        user.setUpdateId("");
        userMapper.insert(user);
        return true;
    }

    @Override
    public Boolean generatePhoneVerifyCode(String phone) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }
        String verifyCode = sb.toString();
        if (MessageUtils.sendMessage(phone, verifyCode)) {
            userCacheService.setPhoneVerifyCode(phone, verifyCode, 5L);
            return true;
        }
        return false;
    }

    @Override
    public void generateImageVerifyCode(HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        String verifyCode = ImageVerifyCodeUtils.outputVerifyImage(130, 30, servletOutputStream, 6);
        userCacheService.setImageVerifyCode(UserContext.get().getUserId(), verifyCode, 5L);
        servletOutputStream.close();
    }

    @Override
    public UserInfoVo getUserInfo(String id) {
        if (!StringUtils.hasLength(id)) {
            throw new CommonException(UserServerErrorResult.USER_NOT_EXIST);
        }
        User user = userMapper.selectById(id);
        return buildUserVo(user);
    }

    private UserInfoVo buildUserVo(User user) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }

    @Override
    public Page<UserInfoVo> getUserList(UserQueryPage queryPage) {
        Page<User> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<User> userList = userMapper.getUserList(page, queryPage);
        page.setRecords(userList);
        return PageUtils.wrap(page, UserInfoVo.class);
    }

    @Override
    public Boolean updateUser(UserInfoVo userInfoVo) {
        User user = userMapper.selectById(userInfoVo.getId());
        if (user == null) {
            throw new CommonException(UserServerErrorResult.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userInfoVo, user);
        userMapper.updateById(user);
        return true;
    }

    @Override
    public Boolean updateUserPassword(UsernamePasswordVo usernamePasswordVo) {
        User user = userMapper.selectById(usernamePasswordVo.getId());
        if (user == null) {
            throw new CommonException(UserServerErrorResult.USER_NOT_EXIST);
        }
        String phone = usernamePasswordVo.getPhone();
        String phoneVerifyCode = usernamePasswordVo.getPhoneVerifyCode();
        if (!StringUtils.hasLength(phoneVerifyCode)) {
            throw new CommonException(UserServerErrorResult.VERIFY_CODE_ERROR);
        }
        String phoneVerifyCodeCache = userCacheService.getPhoneVerifyCode(phone);
        if (!StringUtils.hasLength(phoneVerifyCodeCache)) {
            throw new CommonException(UserServerErrorResult.VERIFY_CODE_OVERDUE);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(User::getPassword, passwordEncoder.encode(usernamePasswordVo.getPassword())).eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
        userCacheService.deletePhoneVerifyCode(phone);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateUserRole(UserRoleRelationVo userRoleRelationVo) {
        User user = userMapper.selectById(userRoleRelationVo.getUserId());
        if (user == null) {
            throw new CommonException(UserServerErrorResult.USER_NOT_EXIST);
        }
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleRelation::getUserId, userRoleRelationVo.getUserId());
        userRoleRelationMapper.delete(queryWrapper);
        List<String> roleIds = userRoleRelationVo.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                UserRoleRelation userRoleRelation = new UserRoleRelation();
                userRoleRelation.setId(CommonUtils.uuid());
                userRoleRelation.setUserId(userRoleRelationVo.getUserId());
                userRoleRelation.setRoleId(roleId);
                userRoleRelationMapper.insert(userRoleRelation);
            }
        }
        return true;
    }

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