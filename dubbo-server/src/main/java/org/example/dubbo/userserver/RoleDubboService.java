package org.example.dubbo.userserver;

import org.example.dubbo.userserver.entity.RoleDubboVO;

import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
public interface RoleDubboService {
    List<RoleDubboVO> getRoleByUserId(String userId);
}