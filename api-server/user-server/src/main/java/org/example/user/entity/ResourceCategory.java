package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("resource_category")
@EqualsAndHashCode(callSuper = true)
public class ResourceCategory extends BaseEntity {
    private String name;
    private Integer sort;
    private String description;
}