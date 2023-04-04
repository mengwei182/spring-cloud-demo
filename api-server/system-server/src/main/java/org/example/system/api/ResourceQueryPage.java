package org.example.system.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.model.QueryPage;

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