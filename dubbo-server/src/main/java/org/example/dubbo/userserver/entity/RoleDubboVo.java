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
public class RoleDubboVo extends BaseEntity {
    private String name;
    private String parentId;
    private String idChain;
    private Integer level;
    private Integer sort;
    private String icon;
    private Integer status;
    private String description;
}