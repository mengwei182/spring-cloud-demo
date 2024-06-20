package org.example.system.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.core.domain.QueryPage;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryPage extends QueryPage {
    private String name;
}