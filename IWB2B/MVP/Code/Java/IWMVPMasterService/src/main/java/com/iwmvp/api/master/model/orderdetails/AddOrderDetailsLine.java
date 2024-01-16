package com.iwmvp.api.master.model.orderdetails;

import lombok.Data;

@Data
public class AddOrderDetailsLine {
    private String languageId;
    private String companyId;
    private Long orderId;
    private String referenceNo;
    private String commodityName;
    private String noOfPieces;
    private String declaredValue;
    private String dimensionUnit;
    private String length;
    private String width;
    private String height;
    private String weightUnit;
    private String weight;
    private Long deletionIndicator;
}
