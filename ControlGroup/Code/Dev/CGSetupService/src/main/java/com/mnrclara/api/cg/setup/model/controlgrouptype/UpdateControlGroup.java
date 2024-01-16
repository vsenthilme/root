package com.mnrclara.api.cg.setup.model.controlgrouptype;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class UpdateControlGroup {

    private String languageId;

    private Long versionNumber;

    private String companyId;

    private Long groupTypeId;

    private String groupTypeName;

    private String companyIdAndDescription;

    private Long statusId;

    private Long statusId2;

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

    private Date validityDateFrom;

    private Date validityDateTo;

    private String relationship;
}
