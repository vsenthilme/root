package com.mnrclara.api.cg.setup.model.state;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindState {

    private List<String> stateId;
    private List<String> stateName;
    private List<String> countryId;
    private List<String> languageId;
    private List<String> companyId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
