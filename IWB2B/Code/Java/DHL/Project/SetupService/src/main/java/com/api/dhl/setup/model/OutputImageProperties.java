package com.api.dhl.setup.model;

import lombok.Data;

import java.util.List;

@Data
public class OutputImageProperties {

    private Boolean splitInvoiceAndReceipt;
    private Boolean splitDocumentsByPages;
    private Boolean splitTransportAndWaybillDocLabels;
    private Long printerDPI;
    private String encodingFormat;
    private List<ImageOptions> imageOptions;
}
