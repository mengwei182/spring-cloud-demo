package org.example.system.service.cache;

import org.example.common.entity.system.Role;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface RoleCacheService {
    List<Role> getRoleByUserId(String userId);

    void setRoleByUserId(String userId, List<Role> roles);
}