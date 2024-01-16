package com.tekclover.wms.core.batch.dto;

import lombok.Data;

@Data
public class StatusId {
    private String languageId;
    private Long statusId;
    private String status;
    private Long deletionIndicator;
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
    private String createdBy;


    /**
     *
     * @param languageId
     * @param statusId
     * @param status
     * @param deletionIndicator
     * @param referenceField1
     * @param referenceField2
     * @param referenceField3
     * @param referenceField4
     * @param referenceField5
     * @param referenceField6
     * @param referenceField7
     * @param referenceField8
     * @param referenceField9
     * @param referenceField10
     * @param createdBy
     */
    public StatusId (String languageId,Long statusId,String status,Long deletionIndicator,String referenceField1,
                       String referenceField2,String referenceField3,String referenceField4,String referenceField5,
                       String referenceField6,String referenceField7,String referenceField8,String referenceField9,
                       String referenceField10,String createdBy) {
        this.languageId = languageId;
        this.statusId = statusId;
        this.status = status;
        this.deletionIndicator = deletionIndicator;
        this.referenceField1 = referenceField1;
        this.referenceField2 = referenceField2;
        this.referenceField3 = referenceField3;
        this.referenceField4 = referenceField4;
        this.referenceField5 = referenceField5;
        this.referenceField6 = referenceField6;
        this.referenceField7 = referenceField7;
        this.referenceField8 = referenceField8;
        this.referenceField9 = referenceField9;
        this.referenceField10 = referenceField10;
        this.createdBy = createdBy;

    }

    }
