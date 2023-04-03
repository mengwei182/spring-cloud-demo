package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuVo extends BaseEntity {
    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    @Size(min = 1, max = 255, message = "名称应该在1-255字符之间")
    private String name;
    /**
     * 路由地址
     */
    @NotNull(message = "路由地址不能为空")
    @Size(min = 1, max = 255, message = "路由地址应该在1-255字符之间")
    private String routeAddress;
    /**
     * 父级id
     */
    private String parentId;
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