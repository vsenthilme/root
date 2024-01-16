package com.tekclover.wms.core.model.transaction;

import lombok.Data;
import java.util.Date;

@Data
public class PutAwayHeaderV2 extends PutAwayHeader {
	
	private Double inventoryQuantity;
	private String barcodeId;
	private Date manufacturerDate;
	private Date expiryDate;
	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String brand;
	private Double orderQty;
	private String cbm;
	private String cbmUnit;
	private Double cbmQuantity;
	private String approvalStatus;
	private String remark;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
	private String actualPackBarcodes;
}
