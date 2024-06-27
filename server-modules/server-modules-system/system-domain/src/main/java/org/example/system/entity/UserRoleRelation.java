package org.example.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;

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
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
}