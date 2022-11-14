package org.example.dubbo.userserver;

import org.example.dubbo.userserver.entity.Role;

import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
public interface RoleDubboService {
    List<Role> getRoleByUserId(String userId);
}