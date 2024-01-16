package com.tekclover.wms.core.batch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Inventory {

	private Long inventoryId;
	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String palletCode;
	private String caseCode;
	private String itemCode;
	private String packBarcode;
	private Long variantCode;
	private String variantSubCode;
	private String batchSerialNumber;
	private String storageBin;
	private Long stockTypeId;
	private Long specialStockIndicatorId;
	private String referenceOrderNo;
	private String storageMethod;
	private Long binClassId;
	private String description;
	private Double inventoryQuantity;
	private Double allocatedQuantity;
	private String inventoryUom;
	private String manufacturerCode;
	private Date manufacturerDate;
	private Date expiryDate;
	private Long deletionIndicator;
	private String dType;
	private String createdBy;

	// V2 fields
	private String barcodeId;
	private String cbm;
	private String cbmUnit;
	private String cbmPerQuantity;
	private String manufacturerName;
	private String origin;
	private String brand;
	private String referenceDocumentNo;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;

	/**
	 * @param languageId
	 * @param companyCodeId
	 * @param plantId
	 * @param warehouseId
	 * @param palletCode
	 * @param caseCode
	 * @param itemCode
	 * @param packBarcode
	 * @param variantCode
	 * @param variantSubCode
	 * @param batchSerialNumber
	 * @param storageBin
	 * @param stockTypeId
	 * @param specialStockIndicatorId
	 * @param referenceOrderNo
	 * @param storageMethod
	 * @param binClassId
	 * @param description
	 * @param allocatedQuantity
	 * @param inventoryQuantity
	 * @param inventoryUom
	 * @param manufacturerCode
	 * @param manufacturerDate
	 * @param expiryDate
	 * @param barcodeId
	 * @param cbm
	 * @param cbmUnit
	 * @param cbmPerQuantity
	 * @param manufacturerName
	 * @param origin
	 * @param brand
	 * @param referenceDocumentNo
	 * @param companyDescription
	 * @param plantDescription
	 * @param warehouseDescription
	 * @param statusDescription
	 * @param deletionIndicator
	 * @param dType
	 * @param createdBy
	 */
	public Inventory(Long inventoryId, String languageId, String companyCodeId, String plantId, String warehouseId, String palletCode, String caseCode,
					 String itemCode, String packBarcode, Long variantCode, String variantSubCode, String batchSerialNumber,
					 String storageBin, Long stockTypeId, Long specialStockIndicatorId, String referenceOrderNo, String storageMethod,
					 Long binClassId, String description, Double allocatedQuantity, Double inventoryQuantity, String inventoryUom,
					 String manufacturerCode, Date manufacturerDate, Date expiryDate, String barcodeId, String cbm, String cbmUnit, String cbmPerQuantity,
					 String manufacturerName, String origin, String brand, String referenceDocumentNo, String companyDescription,
					 String plantDescription, String warehouseDescription, String statusDescription, Long deletionIndicator, String dType, String createdBy) {

		this.inventoryId = inventoryId;
		this.languageId = languageId;
		this.companyCodeId = companyCodeId;
		this.plantId = plantId;
		this.warehouseId = warehouseId;
		this.palletCode = palletCode;
		this.caseCode = caseCode;
		this.itemCode = itemCode;
		this.packBarcode = packBarcode;
		this.variantCode = variantCode;
		this.variantSubCode = variantSubCode;
		this.batchSerialNumber = batchSerialNumber;
		this.storageBin = storageBin;
		this.stockTypeId = stockTypeId;
		this.specialStockIndicatorId = specialStockIndicatorId;
		this.referenceOrderNo = referenceOrderNo;
		this.storageMethod = storageMethod;
		this.binClassId = binClassId;
		this.description = description;
		this.allocatedQuantity = allocatedQuantity;
		this.inventoryQuantity = inventoryQuantity;
		this.inventoryUom = inventoryUom;
		this.manufacturerCode = manufacturerCode;
		this.manufacturerDate = manufacturerDate;
		this.expiryDate = expiryDate;
		this.barcodeId = barcodeId;
		this.cbm = cbm;
		this.cbmUnit = cbmUnit;
		this.cbmPerQuantity = cbmPerQuantity;
		this.manufacturerName = manufacturerName;
		this.origin = origin;
		this.brand = brand;
		this.referenceDocumentNo = referenceDocumentNo;
		this.companyDescription = companyDescription;
		this.plantDescription = plantDescription;
		this.warehouseDescription = warehouseDescription;
		this.statusDescription = statusDescription;
		this.deletionIndicator = deletionIndicator;
		this.dType = dType;
		this.createdBy = createdBy;
	}
}
