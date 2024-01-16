package com.api.dhl.setup.model;

import lombok.Data;

import java.util.List;

@Data
public class ExportDeclaration {

    private List<LineItems> lineItems;
    private Invoice invoice;
    private String placeOfIncoterm;
}
