package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * 资源信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("resource")
@EqualsAndHashCode(callSuper = true)
public class Resource extends BaseEntity {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源URL
     */
    private String url;
    /**
     * 描述
     */
    private String description;
    /**
     * 资源分类id
     */
    private String categoryId;
    /**
     * 状态
     */
    private Integer status;
}