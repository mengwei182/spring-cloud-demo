package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.entity.BaseEntity;

/**
 * 用户角色关联表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("user_role_relation")
@EqualsAndHashCode(callSuper = true)
public class UserRoleRelation extends BaseEntity {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;
}