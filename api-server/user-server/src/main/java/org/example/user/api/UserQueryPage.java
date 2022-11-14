package org.example.user.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.model.QueryPage;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryPage extends QueryPage {
    private String username;
}