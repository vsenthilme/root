package com.api.dhl.setup.model;

import lombok.Data;

@Data
public class CustomerDetails {

    private ShipperDetails shipperDetails;
    private ReceiverDetails receiverDetails;
}
