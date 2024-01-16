package com.mnrclara.api.cg.setup.model.cgentity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindCgEntity {

    private List<Long> entityId;
    private List<Long> clientId;
    private List<String> companyId;
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
