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
@TableName("role_menu_relation")
@EqualsAndHashCode(callSuper = true)
public class RoleMenuRelation extends BaseEntity {
    // 角色id
    private String roleId;
    // 菜单id
    private String menuId;
}