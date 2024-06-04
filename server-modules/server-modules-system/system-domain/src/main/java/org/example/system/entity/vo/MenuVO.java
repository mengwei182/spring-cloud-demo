package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.constant.CommonConstant;
import org.example.common.core.domain.BaseEntity;
import org.example.common.core.tree.TreeModelField;
import org.example.common.core.tree.TreeModelFieldEnum;
import org.example.system.constant.SystemServerConstant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuVO extends BaseEntity {
    /**
     * 名称
     */
    @NotNull(message = CommonConstant.NAME_NOT_NULL)
    @TreeModelField(TreeModelFieldEnum.LABEL)
    @Size(min = 1, max = 255, message = CommonConstant.NAME_LENGTH_ERROR)
    private String name;
    /**
     * 路由地址
     */
    @NotNull(message = SystemServerConstant.PATH_NOT_NULL)
    @Size(min = 1, max = 255, message = SystemServerConstant.PATH_LENGTH_ERROR)
    private String path;
    /**
     * 组件
     */
    private String component;
    /**
     * 父级id
     */
    @TreeModelField(TreeModelFieldEnum.PARENT_ID)
    private String parentId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 0显示，1隐藏
     */
    private Integer hided;
    /**
     * 描述
     */
    private String description;
    /**
     * 子集
     */
    @TreeModelField(TreeModelFieldEnum.CHILDREN)
    private List<MenuVO> children;
}