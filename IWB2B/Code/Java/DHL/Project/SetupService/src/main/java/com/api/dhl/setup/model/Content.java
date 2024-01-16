package com.api.dhl.setup.model;

import lombok.Data;

import java.util.List;

@Data
public class Content {

    private ExportDeclaration exportDeclaration;
    private String unitOfMeasurement;
    private Boolean isCustomsDeclarable;
    private String incoterm;
    private String description;
    private List<Packages> packages;
    private String declaredValueCurrency;
    private Double declaredValue;
}
