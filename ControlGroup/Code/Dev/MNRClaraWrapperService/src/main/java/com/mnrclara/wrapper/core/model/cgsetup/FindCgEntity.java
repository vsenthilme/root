package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.List;

@Data
public class FindCgEntity {

    private List<Long> entityId;
    private List<Long> clientId;
    private List<String> companyId;
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
}
