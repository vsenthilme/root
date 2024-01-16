package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryMovementV2 extends InventoryMovement {

	private String description;
	private Long statusId;
	private Date documentDate;
	private Double openingStock;
	private Double inboundQuantity;
	private Double outboundQuantity;
	private Double closingStock;
	private String updatedBy;
	private Date updatedOn;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
