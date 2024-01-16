package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.BinLocation;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BinLocationFieldSetMapper implements FieldSetMapper<BinLocation> {

	@Override
	public BinLocation mapFieldSet(FieldSet fieldSet) {
		return new BinLocation(
				fieldSet.readString("languageId"),
				fieldSet.readString("companyCodeId"),
				fieldSet.readString("plantId"),
				fieldSet.readString("warehouseId"),
				fieldSet.readString("storageBin"),
				fieldSet.readLong("floorId"),
				fieldSet.readString("storageSectionId"),
				fieldSet.readString("rowId"),
				fieldSet.readString("aisleNumber"),
				fieldSet.readString("spanId"),
				fieldSet.readString("shelfId"),
				fieldSet.readString("binSectionId"),
				fieldSet.readLong("storageTypeId"),
				fieldSet.readLong("binClassId"),
				fieldSet.readString("description"),
				fieldSet.readString("binBarcode"),
				fieldSet.readBoolean("putawayBlock"),
				fieldSet.readBoolean("pickingBlock"),
				fieldSet.readString("blockReason"),
				fieldSet.readLong("statusId"),
				fieldSet.readString("occupiedVolume"),
				fieldSet.readString("occupiedWeight"),
				fieldSet.readString("occupiedQuantity"),
				fieldSet.readString("remainingVolume"),
				fieldSet.readString("remainingWeight"),
				fieldSet.readString("remainingQuantity"),
				fieldSet.readString("totalVolume"),
				fieldSet.readString("totalWeight"),
				fieldSet.readString("totalQuantity"),
				fieldSet.readBoolean("capacityCheck"),
				fieldSet.readDouble("allocatedVolume"),
				fieldSet.readLong("deletionIndicator"),
				fieldSet.readString("dType"),
				fieldSet.readString("createdBy")
		);
	}
}
