package com.tekclover.wms.core.batch.mapper.v2;

import com.tekclover.wms.core.batch.dto.v2.IMPartnerV2;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class IMPartnerV2FieldSetMapper implements FieldSetMapper<IMPartnerV2> {

	@Override
	public IMPartnerV2 mapFieldSet(FieldSet fieldSet) {
		return new IMPartnerV2(
				fieldSet.readString("languageId"),
				fieldSet.readString("companyCodeId"),
				fieldSet.readString("plantId"),
				fieldSet.readString("warehouseId"),
				fieldSet.readString("itemCode"),
				fieldSet.readString("businessPartnerCode"),
				fieldSet.readString("businessPartnerType"),
				fieldSet.readString("partnerItemBarcode"),
				fieldSet.readString("manufacturerCode"),
				fieldSet.readString("manufacturerName"),
				fieldSet.readString("brandName"),
				fieldSet.readString("partnerName"),
				fieldSet.readString("partnerItemNo"),
				fieldSet.readString("vendorItemBarcode"),
				fieldSet.readString("mfrBarcode"),
				fieldSet.readDouble("stock"),
				fieldSet.readString("stockUom"),
				fieldSet.readLong("statusId"),
				fieldSet.readString("referenceField1"),
				fieldSet.readString("referenceField2"),
				fieldSet.readString("referenceField3"),
				fieldSet.readString("referenceField4"),
				fieldSet.readString("referenceField5"),
				fieldSet.readString("referenceField6"),
				fieldSet.readString("referenceField7"),
				fieldSet.readString("referenceField8"),
				fieldSet.readString("referenceField9"),
				fieldSet.readString("referenceField10"),
				fieldSet.readLong("deletionIndicator"),
				fieldSet.readString("createdBy"));
	}
}