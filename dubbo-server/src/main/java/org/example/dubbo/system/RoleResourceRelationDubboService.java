package org.example.dubbo.system;

import org.example.common.entity.system.vo.RoleResourceRelationVo;

import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
public interface RoleResourceRelationDubboService {
    List<RoleResourceRelationVo> getRoleResourceRelations();
}