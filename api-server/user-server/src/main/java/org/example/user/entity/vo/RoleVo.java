package org.example.user.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVo extends BaseEntity {
    private String name;
    private String parentId;
    private Integer level;
    private Integer sort;
    private String icon;
    private Integer status;
    private String description;
}