package com.api.dhl.setup.model;

import lombok.Data;

import java.util.List;

@Data
public class LineItems {

    private Long number;
    private List<CommodityCodes> commodityCodes;
    private Quantity quantity;
    private Double price;
    private String description;
    private Weight weight;
    private Boolean isTaxesPaid;
    private String exportReasonType;
    private String manufacturerCountry;
}
