package com.mnrclara.api.cg.setup.model.controlgroup;

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
    private Date fromDate;
    private Date toDate;
}
