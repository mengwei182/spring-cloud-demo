package org.example.common.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.base.BaseEntity;
import org.example.common.util.annotation.TreeModelFieldEnum;
import org.example.common.util.annotation.TreeModelField;

/**
 * 菜单信息表
 *
 * @author lihui
 * @since 2022/10/29
 */
@Data
@TableName("menu")
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {
    /**
     * 名称
     */
    @TreeModelField(field = TreeModelFieldEnum.NAME)
    private String name;
    /**
     * 路由地址
     */
    private String routeAddress;
    /**
     * 父级id
     */
    @TreeModelField(field = TreeModelFieldEnum.PARENT_ID)
    private String parentId;
    /**
     * id链
     */
    private String idChain;
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
}