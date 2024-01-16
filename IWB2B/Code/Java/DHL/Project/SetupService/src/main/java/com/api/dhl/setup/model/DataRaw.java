package com.api.dhl.setup.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataRaw {

    private Boolean getOptionalInformation;
    private List<CustomerReferences> customerReferences;
    private Pickup pickup;
    private OutputImageProperties outputImageProperties;
    private Content content;
    private String productCode;
    private String localProductCode;
    private Date plannedShippingDateAndTime;
    private Boolean requestOnDemandDeliveryURL;
    private List<Accounts> accounts;
    private Boolean getRateEstimates;
    private CustomerDetails customerDetails;
    private List<ValueAddedServices> valueAddedServices;
}
