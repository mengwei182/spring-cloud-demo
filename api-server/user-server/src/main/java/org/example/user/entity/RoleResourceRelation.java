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
@TableName("role_resource_relation")
@EqualsAndHashCode(callSuper = true)
public class RoleResourceRelation extends BaseEntity {
    private String roleId;
    private String resourceId;
}