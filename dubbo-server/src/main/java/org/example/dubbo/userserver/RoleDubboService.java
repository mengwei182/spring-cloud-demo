package org.example.dubbo.userserver;

import org.example.common.entity.system.vo.RoleVo;

import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
public interface RoleDubboService {
    List<RoleVo> getRoleByUserId(String userId);
}