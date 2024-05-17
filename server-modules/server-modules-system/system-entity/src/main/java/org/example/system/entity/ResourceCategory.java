package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.entity.BaseEntity;

/**
 * 资源分类信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("resource_category")
@EqualsAndHashCode(callSuper = true)
public class ResourceCategory extends BaseEntity {
    /**
     * 分类名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 0禁用，1正常
     */
    private Integer status;
}