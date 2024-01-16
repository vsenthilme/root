package com.iwmvp.api.master.model.orderdetails;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindOrderDetailsHeader {
    private List<String> companyId;
    private List<Long> orderId;
    private List<String> referenceNo;
    private List<String> shipsyOrderNo;
    private List<Long> customerId;
    private List<String> loadType;
    private List<String> typeOfDelivery;
    private List<String> originDetailsName;
    private List<String> originDetailsPincode;
    private List<String> destinationDetailsName;
    private List<String> destinationDetailsPincode;
    private List<String> serviceTypeId;
    private List<String> originCity;
    private List<String> originState;
    private List<String> originCountry;
    private List<String> destinationCity;
    private List<String> destinationState;
    private List<String> destinationCountry;
    private List<String> status;
    private List<String> createdBy;
    private Date startDate;
    private Date endDate;
}
