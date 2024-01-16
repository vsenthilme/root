package com.tekclover.wms.api.transaction.model.inbound.staging.v2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLineEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class StagingLineEntityV2 extends StagingLineEntity {

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;

	@Column(name = "INV_QTY")
	private Double inventoryQuantity;

	@Column(name = "MFR_CODE", columnDefinition = "nvarchar(255)")
	private String manufacturerCode;
	
	@Column(name = "MFR_NAME", columnDefinition = "nvarchar(255)")
	private String manufacturerName;
	
	@Column(name = "ORIGIN", columnDefinition = "nvarchar(255)")
	private String origin;
	
	@Column(name = "BRAND", columnDefinition = "nvarchar(255)")
	private String brand;

	@Column(name = "PARTNER_ITEM_BARCODE", columnDefinition = "nvarchar(500)")
	private String partner_item_barcode;

	@Column(name = "REC_ACCEPT_QTY")
	private Double rec_accept_qty;

	@Column(name = "REC_DAMAGE_QTY")
	private Double rec_damage_qty;
}
