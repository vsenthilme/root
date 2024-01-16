package com.tekclover.wms.core.model.masters;

import lombok.Data;
import java.util.Date;

@Data
public class Route {

    private String languageId;

    private String companyCodeId;

    private String plantId;

    private String warehouseId;

    private Long routeId;

    private String routeName;

    private String legId;

    private String legName;

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

    private String createdBy;

    private Date createdOn;

    private String updatedBy;

    private Date updatedOn;
}
