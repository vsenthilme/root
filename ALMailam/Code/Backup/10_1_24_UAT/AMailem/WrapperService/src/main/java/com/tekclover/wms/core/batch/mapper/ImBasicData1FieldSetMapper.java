package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.ImBasicData1;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class ImBasicData1FieldSetMapper implements FieldSetMapper<ImBasicData1> {

	@Override
	public ImBasicData1 mapFieldSet(FieldSet fieldSet) {
		return new ImBasicData1(
				fieldSet.readString("uomId"),
				fieldSet.readString("languageId"),
				fieldSet.readString("companyCodeId"),
				fieldSet.readString("plantId"),
				fieldSet.readString("warehouseId"),
				fieldSet.readString("itemCode"),
				fieldSet.readString("manufacturerPartNo"),
				fieldSet.readString("description"),
				fieldSet.readString("model"),
				fieldSet.readString("specifications1"),
				fieldSet.readString("specifications2"),
				fieldSet.readString("eanUpcNo"),
				fieldSet.readString("hsnCode"),
				fieldSet.readLong("itemType"),
				fieldSet.readLong("itemGroup"),
				fieldSet.readLong("subItemGroup"),
				fieldSet.readString("storageSectionId"),
				fieldSet.readDouble("totalStock"),
				fieldSet.readDouble("minimumStock"),
				fieldSet.readDouble("maximumStock"),
				fieldSet.readDouble("reorderLevel"),
				fieldSet.readBoolean("capacityCheck"),
				fieldSet.readDouble("replenishmentQty"),
				fieldSet.readDouble("safetyStock"),
				fieldSet.readString("capacityUnit"),
				fieldSet.readString("capacityUom"),
				fieldSet.readString("quantity"),
				fieldSet.readDouble("weight"),
				fieldSet.readBoolean("shelfLifeIndicator"),
				fieldSet.readLong("statusId"),
				fieldSet.readDouble("length"),
				fieldSet.readDouble("width"),
				fieldSet.readDouble("height"),
				fieldSet.readString("dimensionUom"),
				fieldSet.readDouble("volume"),
				fieldSet.readString("manufacturerName"),
				fieldSet.readString("manufacturerFullName"),
				fieldSet.readString("manufacturerCode"),
				fieldSet.readString("brand"),
				fieldSet.readString("supplierPartNumber"),
				fieldSet.readString("remarks"),
				fieldSet.readString("dType"),
				fieldSet.readLong("deletionIndicator"),
				fieldSet.readString("createdBy")
		);
	}
}