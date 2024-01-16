package com.tekclover.wms.core.model.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Inventory { 
	
	protected String languageId;
	protected String companyCodeId;
	protected String plantId;
	protected String warehouseId;
	protected String palletCode;
	protected String caseCode;
	protected String packBarcodes;
	protected String itemCode;
	protected Long variantCode;
	protected String variantSubCode;
	protected String batchSerialNumber;
	protected String storageBin;
	protected Long stockTypeId;
	protected Long specialStockIndicatorId;
	protected String referenceOrderNo;
	protected String storageMethod;
	protected Long binClassId;
	protected String description;
	protected Double inventoryQuantity;
	protected Double allocatedQuantity;
	protected String inventoryUom;
	protected Date manufacturerDate;
	protected Date expiryDate;
	protected Long deletionIndicator;
	protected String referenceField1;
	protected String referenceField2;
	protected String referenceField3;
	protected String referenceField4;
	protected String referenceField5;
	protected String referenceField6;
	protected String referenceField7;
	protected String referenceField8;
	protected String referenceField9;
	protected String referenceField10;
	protected String createdBy;
	protected Date createdOn = new Date();
}
