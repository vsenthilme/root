package com.mnrclara.api.cg.setup.model.relationshipid;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindRelationShipId {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> relationShipId;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;

}
