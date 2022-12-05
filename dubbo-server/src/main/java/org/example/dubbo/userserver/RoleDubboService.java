package org.example.dubbo.userserver;

import org.example.dubbo.userserver.entity.RoleDubboVo;

import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
public interface RoleDubboService {
    List<RoleDubboVo> getRoleByUserId(String userId);
}