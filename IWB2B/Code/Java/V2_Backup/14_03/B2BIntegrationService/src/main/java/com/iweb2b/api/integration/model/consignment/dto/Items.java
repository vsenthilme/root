package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class Items {

    private Long number;
    private String itemType;
    private String itemName;
    private String priceCurrency;
    private String itemValue;
    private String itemUrl;
    private String desc;
}