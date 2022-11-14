package org.example.user.entity.vo;

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
public class UserRoleRelationVo extends BaseEntity {
    private String userId;
    private String roleId;
    List<String> userIds;
    List<String> roleIds;
}