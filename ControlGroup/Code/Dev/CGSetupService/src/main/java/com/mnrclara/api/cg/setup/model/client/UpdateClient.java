package com.mnrclara.api.cg.setup.model.client;

import lombok.Data;

@Data
public class UpdateClient {

    private String companyId;

    private String languageId;

    private Long clientId;

    private String clientName;

    private Long statusId;

    private Long deletionIndicator = 0L;

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
