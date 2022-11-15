package org.example.user.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.entity.BaseEntity;

/**
 * @author lihui
 * @since 2022/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceVO extends BaseEntity {
    private String name;
    private String url;
    private String description;
    private String categoryId;
    private Integer status;
    private String categoryName;
}