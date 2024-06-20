package org.example.system.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.QueryPage;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceQueryPage extends QueryPage {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源分类id
     */
    private String categoryId;
}