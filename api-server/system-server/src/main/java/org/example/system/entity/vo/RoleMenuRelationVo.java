package org.example.system.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("role_menu_relation")
@EqualsAndHashCode(callSuper = true)
public class RoleMenuRelationVo extends BaseEntity {
    // 角色id
    private String roleId;
    // 菜单id
    private String menuId;
    // 角色id集合
    private List<String> roleIds;
    // 菜单id集合
    private List<String> menuIds;
}