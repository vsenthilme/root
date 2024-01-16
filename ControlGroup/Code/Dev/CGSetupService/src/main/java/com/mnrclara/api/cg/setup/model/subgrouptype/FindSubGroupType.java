package com.mnrclara.api.cg.setup.model.subgrouptype;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindSubGroupType {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> groupTypeId;
    private List<Long> subGroupTypeId;
    private List<Long> versionNumber;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
