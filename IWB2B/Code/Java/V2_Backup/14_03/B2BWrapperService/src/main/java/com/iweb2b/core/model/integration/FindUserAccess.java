package com.iweb2b.core.model.integration;
import lombok.Data;

import java.util.List;

@Data
public class FindUserAccess {
    private List<String> userId;
    private List<Long> userRoleId;
    private List<Long> userTypeId;
}
