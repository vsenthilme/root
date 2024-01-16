package com.tekclover.wms.api.transaction.model.inbound.gr.v2;

import com.tekclover.wms.api.transaction.model.inbound.gr.GrLine;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class GrLineV2 extends GrLine {

	@Column(name = "BILL_STATUS", columnDefinition = "nvarchar(100)")
	private String billStatus;

	@Column(name = "LENGTH")
	private Double length;

	@Column(name = "WIDTH")
	private Double width;

	@Column(name = "HEIGHT")
	private Double height;

	@Column(name = "INV_QTY")
	private Double inventoryQuantity;
	
	@Column(name = "BARCODE_ID", columnDefinition = "nvarchar(255)")
	private String barcodeId;
	
	@Column(name = "CBM")
	private Double cbm;
	
	@Column(name = "CBM_UNIT", columnDefinition = "nvarchar(255)")
	private String cbmUnit;
	
	@Column(name = "MFR_CODE", columnDefinition = "nvarchar(255)")
	private String manufacturerCode;
	
	@Column(name = "MFR_NAME", columnDefinition = "nvarchar(255)")
	private String manufacturerName;
	
	@Column(name = "ORIGIN", columnDefinition = "nvarchar(255)")
	private String origin;
	
	@Column(name = "BRAND", columnDefinition = "nvarchar(255)")
	private String brand;
	
	@Column(name = "REJ_TYPE", columnDefinition = "nvarchar(255)")
	private String rejectType;
	
	@Column(name = "REJ_REASON", columnDefinition = "nvarchar(255)")
	private String rejectReason;
	
	@Column(name = "CBM_QTY")
	private Double cbmQuantity;										//CBM per Quantity

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;
	
	@Column(name = "ST_BIN_INTM", columnDefinition = "nvarchar(100)")
	private String interimStorageBin;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;
}
