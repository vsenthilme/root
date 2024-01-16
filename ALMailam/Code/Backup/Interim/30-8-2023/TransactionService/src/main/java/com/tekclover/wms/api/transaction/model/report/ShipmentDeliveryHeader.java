package com.tekclover.wms.api.transaction.model.report;

import java.util.Date;

import lombok.Data;

@Data
public class ShipmentDeliveryHeader {

	private String deliveryTo;
	private Date deliveryDate;
	private Long orderType;
}
