package com.mnrclara.spark.core.model.Almailem;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PeriodicHeaderV2 {

    private String languageId;
    private String companyCode;
    private String plantId;
    private String warehouseId;
    private Long cycleCountTypeId;
    private String cycleCountNo;
    private Long statusId;
    private String referenceField1;
    private String referenceField2;
    private String referenceField3;
    private String referenceField4;
    private String referenceField5;
    private String referenceField6;
    private String referenceField7;
    private String referenceField8;
    private String referenceField9;
    private String referenceField10;
    private Long deletionIndicator;
    private String createdBy;
    private Timestamp createdOn;
    private String countedBy;
    private Timestamp countedOn;
    private String confirmedBy;
    private Timestamp confirmedOn;

    // V2 fields
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;
    private String middlewareId;
    private String middlewareTable;
    private String referenceDocumentType;
    private String referenceCycleCountNo;
}
