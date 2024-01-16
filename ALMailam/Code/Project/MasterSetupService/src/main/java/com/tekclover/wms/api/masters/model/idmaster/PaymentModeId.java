package com.tekclover.wms.api.masters.model.idmaster;

import lombok.Data;

import java.util.Date;
@Data
public class PaymentModeId {
    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private Long paymentModeId;
    private String description;
    private Long statusId;
    private String companyIdAndDescription;
    private String plantIdAndDescription;
    private String warehouseIdAndDescription;
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
    private Date createdOn = new Date();
    private String updatedBy;
    private Date updatedOn = new Date();
}
