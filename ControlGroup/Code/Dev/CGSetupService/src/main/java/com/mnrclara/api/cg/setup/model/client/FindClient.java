package com.mnrclara.api.cg.setup.model.client;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindClient {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> clientId;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
