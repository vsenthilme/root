package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindControlGroup {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> groupId;
    private List<Long> groupTypeId;
    private List<Long> versionNumber;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
}
