package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 用户部门关联信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDepartmentRelationVO extends BaseEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 部门id
     */
    private Long departmentId;
    /**
     * 用户id集合
     */
    private List<Long> userIds;
    /**
     * 部门id集合
     */
    private List<Long> departmentIds;
}