package com.tekclover.wms.core.model.transaction;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class PutAwayLineV2 extends PutAwayLine {

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
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;

	private String middlewareId;
	private String middlewareHeaderId;
	private String middlewareTable;
	private String purchaseOrderNumber;
	private String manufactureFullName;
}