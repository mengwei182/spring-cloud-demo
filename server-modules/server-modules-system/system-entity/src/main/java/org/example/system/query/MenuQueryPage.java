package org.example.system.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.model.QueryPage;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuQueryPage extends QueryPage {
    private String name;
}