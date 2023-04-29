package org.example.common.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.base.BaseEntity;

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
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String description;
}