package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;

/**
 * 角色资源关联表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("role_resource_relation")
@EqualsAndHashCode(callSuper = true)
public class RoleResourceRelation extends BaseEntity {
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 资源id
     */
    private String resourceId;
}