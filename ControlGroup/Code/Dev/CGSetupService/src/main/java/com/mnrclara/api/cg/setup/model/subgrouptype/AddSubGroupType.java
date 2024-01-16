package com.mnrclara.api.cg.setup.model.subgrouptype;

import lombok.Data;

import java.util.Date;

@Data
public class AddSubGroupType {

    private String companyId;

    private String languageId;

    private Long versionNumber;

    private Long groupTypeId;

    private Long subGroupTypeId;

    private String groupTypeName;

    private String subGroupTypeName;

    private Long statusId;

    private Long deletionIndicator;

    private String companyIdAndDescription;

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

    private Date validityDateFrom;

    private Date validityDateTo;

}
