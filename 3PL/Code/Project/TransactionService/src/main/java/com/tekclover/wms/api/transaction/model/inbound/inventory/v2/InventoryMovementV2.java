package com.tekclover.wms.api.transaction.model.inbound.inventory.v2;

import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class InventoryMovementV2 extends InventoryMovement {

	@Column(name = "ITM_TEXT", columnDefinition = "nvarchar(300)")
	private String description;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "DOC_DATE")
	private Date documentDate;
	
	@Column(name = "OPEN_STOCK")
	private Double openingStock;
	
	@Column(name = "IB_QTY")
	private Double inboundQuantity;
	
	@Column(name = "OB_QTY")
	private Double outboundQuantity;
	
	@Column(name = "CLOSE_STOCK")
	private Double closingStock;

	@Column(name = "IM_UTD_BY")
	private String updatedBy;

	@Column(name = "IM_UTD_ON")
	private Date updatedOn;

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;
}
