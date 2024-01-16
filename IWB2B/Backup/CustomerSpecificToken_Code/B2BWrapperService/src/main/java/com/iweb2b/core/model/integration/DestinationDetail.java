package com.iweb2b.core.model.integration;

import lombok.Data;

import java.util.Date;

@Data
public class DestinationDetail {
    private String address_hub_code;
    private String name;
    private String phone;
    private String alternate_phone;
    private String address_line_1;
    private String address_line_2;
    private String pincode;
    private String district;
    private String city;
    private String state;
    private String country;
    private String latitude;
    private String longitude;
}
