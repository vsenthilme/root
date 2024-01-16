package com.tekclover.wms.api.transaction.model.dto;

public interface IInventory {

	public String getStorageBin();
	public Double getInventoryQty();
	public String getInventoryUom();

	public String getLanguageId();
	public String getCompanyCodeId();
	public String getPlantId();
	public String getWarehouseId();
	public String getPalletCode();
	public String getCaseCode();
	public String getItemCode();
	public String getVariantCode();
	public String getVariantSubCode();
	public String getBatchSerialNumber();
	public String getStockTypeId();
	public String getSpecialStockIndicatorId();
	public String getReferenceOrderNo();
	public String getStorageMethod();
	public String getBinClassId();
	public String getDescription();
	public String getInventoryQuantity();
	public String getAllocatedQuantity();
	public String getManufacturerDate();
	public String getExpiryDate();
	public String getReferenceField1();
	public String getReferenceField2();
	public String getReferenceField3();
	public String getReferenceField4();
	public String getReferenceField5();
	public String getReferenceField6();
	public String getReferenceField7();
	public String getReferenceField8();
	public String getReferenceField9();
	public String getReferenceField10();
	public String getCreatedBy();
	public String getCreatedOn();
	public String getUpdatedBy();
	public String getUpdatedOn();
	public String getManufacturerCode();
	public String getBarcodeId();
	public String getCbm();
	public String getCbmUnit();
	public String getCbmPerQuantity();
	public String getManufacturerName();
	public String getOrigin();
	public String getBrand();
	public String getReferenceDocumentNo();
	public String getCompanyDescription();
	public String getPlantDescription();
	public String getWarehouseDescription();
	public String getStatusDescription();
}
