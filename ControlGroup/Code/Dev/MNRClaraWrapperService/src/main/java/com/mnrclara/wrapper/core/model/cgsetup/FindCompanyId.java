package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindCompanyId {
    private List<String> companyId;
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;

}
