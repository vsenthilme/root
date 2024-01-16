package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindControlGroupType {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> groupTypeId;
    private List<Long> versionNumber;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
}
