package com.tekclover.wms.api.transaction.model.outbound.pickup.v2;


import lombok.Data;

import java.util.List;

@Data
public class PickUpHeaderReport {

    private String pickUpHeaderCount;

    private List<PickUpHeaderCount> pickupHeaderV2;
}
