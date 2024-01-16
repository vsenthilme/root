package com.api.dhl.setup.model;

import lombok.Data;

@Data
public class ShipperDetails {

    private PostalAddress postalAddress;
    private ContactInformation contactInformation;
    private String typeCode;
}
