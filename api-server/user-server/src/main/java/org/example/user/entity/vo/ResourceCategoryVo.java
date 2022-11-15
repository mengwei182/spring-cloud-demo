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
public class ResourceCategoryVo extends BaseEntity {
    private String name;
    private Integer sort;
    private String description;
}