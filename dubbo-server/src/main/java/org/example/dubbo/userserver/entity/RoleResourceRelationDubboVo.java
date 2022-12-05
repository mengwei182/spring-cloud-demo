package org.example.dubbo.userserver.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResourceRelationDubboVo extends BaseEntity {
    private String roleId;
    private String resourceId;
}