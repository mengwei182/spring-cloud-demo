package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleRelationVO extends BaseEntity {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 用户id集合
     */
    private List<String> userIds;
    /**
     * 角色id集合
     */
    private List<String> roleIds;
}