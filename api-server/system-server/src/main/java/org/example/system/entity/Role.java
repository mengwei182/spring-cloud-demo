package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * 角色信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 描述
     */
    private String description;
    /**
     * 部门id
     */
    private String departmentId;
}