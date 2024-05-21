package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;

import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceCategoryVO extends BaseEntity {
    /**
     * 分类名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String description;
    /**
     * 资源列表
     */
    private List<ResourceVO> resources;
}