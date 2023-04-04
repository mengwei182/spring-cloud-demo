package org.example.system.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.SystemServerErrorResult;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceVo extends BaseEntity {
    /**
     * 资源名称
     */
    @NotNull(message = CommonErrorResult.NAME_NOT_NULL)
    @Size(min = 1, max = 255, message = CommonErrorResult.NAME_LENGTH_ERROR)
    private String name;
    /**
     * 资源URL
     */
    @NotNull(message = SystemServerErrorResult.RESOURCE_URL_NOT_NULL)
    @Size(min = 1, max = 255, message = SystemServerErrorResult.RESOURCE_URL_LENGTH_ERROR)
    private String url;
    /**
     * 描述
     */
    private String description;
    /**
     * 资源分类id
     */
    @NotNull(message = SystemServerErrorResult.RESOURCE_CATEGORY_ID_NOT_NULL)
    @Size(message = SystemServerErrorResult.RESOURCE_CATEGORY_ID_LENGTH_ERROR)
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