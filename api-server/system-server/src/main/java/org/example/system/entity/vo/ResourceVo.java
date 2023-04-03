package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceVo extends BaseEntity {
    // 资源名称
    private String name;
    // 资源URL
    private String url;
    /**
     * 描述
     */
    private String description;
    // 资源分类id
    private String categoryId;
    /**
     * 状态
     */
    private Integer status;
    // 资源分类名称
    private String categoryName;
}