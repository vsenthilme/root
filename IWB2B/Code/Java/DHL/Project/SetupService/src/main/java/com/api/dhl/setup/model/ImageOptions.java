package com.api.dhl.setup.model;

import lombok.Data;

@Data
public class ImageOptions {

    private String templateName;
    private String invoiceType;
    private String languageCode;
    private Boolean isRequested;
    private String typeCode;
    private Boolean hideAccountNumber;
    private Long numberOfCopies;
}
