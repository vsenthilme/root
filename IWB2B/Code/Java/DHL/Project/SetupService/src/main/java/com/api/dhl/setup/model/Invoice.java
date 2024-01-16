package com.api.dhl.setup.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Invoice {

    private Date date;
    private List<String> customerDataTextEntries;
    private String number;
    private List<String> instructions;
    private Double totalGrossWeight;
    private String signatureName;
    private String function;
    private Double totalNetWeight;
    private String signatureTitle;
}
