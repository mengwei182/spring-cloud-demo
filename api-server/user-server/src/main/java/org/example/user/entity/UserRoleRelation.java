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
@TableName("user_role_relation")
@EqualsAndHashCode(callSuper = true)
public class UserRoleRelation extends BaseEntity {
    private String userId;
    private String roleId;
}