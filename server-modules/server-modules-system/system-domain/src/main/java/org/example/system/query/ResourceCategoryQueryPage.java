package org.example.system.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.QueryPage;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceCategoryQueryPage extends QueryPage {
    private String name;
}