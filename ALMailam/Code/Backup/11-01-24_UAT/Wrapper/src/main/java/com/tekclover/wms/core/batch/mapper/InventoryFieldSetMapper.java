package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.Inventory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class InventoryFieldSetMapper implements FieldSetMapper<Inventory> {

	@Override
	public Inventory mapFieldSet(FieldSet fieldSet) {
		return new Inventory(
				fieldSet.readString("languageId"),
				fieldSet.readString("companyCodeId"),
				fieldSet.readString("plantId"),
				fieldSet.readString("warehouseId"),
				fieldSet.readLong("inventoryId"),
				fieldSet.readString("palletCode"),
				fieldSet.readString("caseCode"),
				fieldSet.readString("itemCode"),
				fieldSet.readString("packBarcode"),
				fieldSet.readLong("variantCode"),
				fieldSet.readString("variantSubCode"),
				fieldSet.readString("batchSerialNumber"),
				fieldSet.readString("storageBin"),
				fieldSet.readLong("stockTypeId"),
				fieldSet.readLong("specialStockIndicatorId"),
				fieldSet.readString("referenceOrderNo"),
				fieldSet.readString("storageMethod"),
				fieldSet.readLong("binClassId"),
				fieldSet.readString("description"),
				fieldSet.readDouble("allocatedQuantity"),
				fieldSet.readDouble("inventoryQuantity"),
				fieldSet.readString("inventoryUom"),
				fieldSet.readString("manufacturerCode"),
				fieldSet.readDate("manufacturerDate"),
				fieldSet.readDate("expiryDate"),
				fieldSet.readString("barcodeId"),
				fieldSet.readString("cbm"),
				fieldSet.readString("cbmUnit"),
				fieldSet.readString("cbmPerQuantity"),
				fieldSet.readString("manufacturerName"),
				fieldSet.readString("origin"),
				fieldSet.readString("brand"),
				fieldSet.readString("referenceDocumentNo"),
				fieldSet.readString("companyDescription"),
				fieldSet.readString("plantDescription"),
				fieldSet.readString("warehouseDescription"),
				fieldSet.readString("statusDescription"),
				fieldSet.readLong("deletionIndicator"),
				fieldSet.readString("dType"),
				fieldSet.readString("createdBy")
		);
	}
}