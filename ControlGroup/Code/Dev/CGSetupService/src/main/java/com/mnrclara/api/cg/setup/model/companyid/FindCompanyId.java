package com.mnrclara.api.cg.setup.model.companyid;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindCompanyId {
    private List<String> companyId;
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;

}
