package com.mnrclara.api.cg.setup.model.clientcontrolgroup;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindClientControlGroup {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> clientId;
    private List<Long> groupTypeId;
    private List<Long> subGroupTypeId;
    private List<Long> versionNumber;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
