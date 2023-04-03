package org.example.system.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.model.QueryPage;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuQueryPage extends QueryPage {
    private String name;
}