package com.api.dhl.setup.model;

import lombok.Data;

import java.util.List;

@Data
public class Packages {

    private List<CustomerReferences> customerReferences;
    private List<Identifiers> identifiers;
    private Double weight;
    private String description;
    private Dimensions dimensions;
}
