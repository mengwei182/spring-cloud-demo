package org.example.user.entity.vo;

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
    private String roleId;
    private String menuId;
    private List<String> roleIds;
    private List<String> menuIds;
}