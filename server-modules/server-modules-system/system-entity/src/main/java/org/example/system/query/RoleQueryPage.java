package org.example.system.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.model.QueryPage;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryPage extends QueryPage {
    private String name;
}