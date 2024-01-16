package com.tekclover.wms.core.batch.mapper.v2;

import com.tekclover.wms.core.batch.dto.v2.BinLocationV2;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BinLocationV2FieldSetMapper implements FieldSetMapper<BinLocationV2> {

	@Override
	public BinLocationV2 mapFieldSet(FieldSet fieldSet) {
		return new BinLocationV2(
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
				fieldSet.readLong("deletionIndicator"),
				fieldSet.readString("createdBy"),
				fieldSet.readString("dtype")
		);
	}
}