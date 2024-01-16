package com.mnrclara.wrapper.core.model.spark;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class ControlGroupTypeV1 {

    private Long versionNumber;
    private String languageId;
    private String companyId;
    private String groupTypeId;
    private String groupTypeName;
    private Long statusId;
    private Timestamp validityDateFrom;
    private Timestamp validityDateTo;
    private Timestamp createdOn;
}
