package com.iwmvp.core.model.master;

import lombok.Data;

@Data
public class AddOrderDetailsLine {
    private String commodityName;
    private String noOfPieces;
    private String declaredValue;
    private String dimensionUnit;
    private String length;
    private String width;
    private String height;
    private String weightUnit;
    private String weight;
}
