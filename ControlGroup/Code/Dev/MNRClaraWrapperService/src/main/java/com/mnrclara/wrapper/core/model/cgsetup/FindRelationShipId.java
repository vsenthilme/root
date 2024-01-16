package com.mnrclara.wrapper.core.model.cgsetup;

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

}
