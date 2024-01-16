package com.mnrclara.wrapper.core.model.cgsetup;

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
}
