package com.iwmvp.api.master.model.loyaltysetup;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddLoyaltySetup {
    private String languageId;
    private String companyId;
    private String categoryId;
    private Double transactionValueFrom;
    private Double transactionValueTo;
    private Double loyaltyPoint;
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
}
