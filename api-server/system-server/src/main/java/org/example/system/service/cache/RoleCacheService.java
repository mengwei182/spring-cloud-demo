package org.example.system.service.cache;

import org.example.system.entity.Role;

import java.util.List;

public interface RoleCacheService {
    List<Role> getRoleByUserId(String userId);

    void setRoleByUserId(String userId, List<Role> roles);
}