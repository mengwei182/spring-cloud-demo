package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.constant.CommonConstant;
import org.example.common.core.domain.BaseEntity;
import org.example.system.constant.SystemServerConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceVO extends BaseEntity {
    /**
     * 资源名称
     */
    @NotBlank(message = CommonConstant.NAME_NOT_NULL)
    @Size(min = 1, max = 255, message = CommonConstant.NAME_LENGTH_ERROR)
    private String name;
    /**
     * 资源URL
     */
    @NotBlank(message = SystemServerConstant.RESOURCE_URL_NOT_NULL)
    @Size(min = 1, max = 255, message = SystemServerConstant.RESOURCE_URL_LENGTH_ERROR)
    private String url;
    /**
     * 描述
     */
    private String description;
    /**
     * 资源分类id
     */
    @NotBlank(message = SystemServerConstant.RESOURCE_CATEGORY_ID_NOT_NULL)
    @Size(min = 1, max = 32, message = SystemServerConstant.RESOURCE_CATEGORY_ID_LENGTH_ERROR)
    private String categoryId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 资源分类名称
     */
    private String categoryName;
}