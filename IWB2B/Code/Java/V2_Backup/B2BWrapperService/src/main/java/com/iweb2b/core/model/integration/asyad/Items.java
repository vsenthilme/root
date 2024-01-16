package com.iweb2b.core.model.integration.asyad;

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