package com.api.dhl.setup.model;

import lombok.Data;

@Data
public class PostalAddress {
    private String cityName;
    private String countryCode;
    private String provinceCode;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String countryName;
}
