package com.mnrclara.wrapper.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindControlGroupTypeV1 {

    private List<String> companyId;
    private List<String> languageId;
    private List<String> groupTypeId;
    private List<Long> versionNumber;
    private List<Long> statusId;
    private Date startCreatedOn;
    private Date endCreatedOn;
}
