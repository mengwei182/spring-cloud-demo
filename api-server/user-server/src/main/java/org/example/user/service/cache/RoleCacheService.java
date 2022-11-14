package org.example.user.service.cache;

import org.example.user.entity.Role;

import java.util.List;

public interface RoleCacheService {
    List<Role> getRoleByUserId(String userId);

    void setRoleByUserId(String userId, List<Role> roles);
}