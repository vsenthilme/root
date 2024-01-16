package com.iweb2b.api.integration.model.usermanagement;
import lombok.Data;

import java.util.List;

@Data
public class FindUserAccess {
    private List<String> userId;
    private List<String> languageId;
    private List<String> companyCode;
    private List<Long> userRoleId;
    private List<Long> userTypeId;
}
