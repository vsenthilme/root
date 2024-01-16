package com.tekclover.wms.api.transaction.model.deliverymodule.deliveryheader;

import lombok.Data;

@Data
public class AddDeliveryHeader {

    private String languageId;

    private String companyCodeId;

    private String plantId;

    private String warehouseId;

    private String deliveryNo;

    private String vehicleNo;

    private String driverId;

    private String driverName;

    private String routeId;

    private String deliveryFailureReason;

    private String statusId;

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
}
