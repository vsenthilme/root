package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.ErrorLog;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class ErrorLogFieldSetMapper implements FieldSetMapper<ErrorLog> {

	@Override
	public ErrorLog mapFieldSet(FieldSet fieldSet) {
		return new ErrorLog(
				fieldSet.readString("orderId"),
				fieldSet.readString("orderTypeId"),
				fieldSet.readDate("orderDate"),
				fieldSet.readString("errorMessage"),
				fieldSet.readString("languageId"),
				fieldSet.readString("companyCode"),
				fieldSet.readString("plantId"),
				fieldSet.readString("warehouseId"),
				fieldSet.readString("refDocNumber"),
				fieldSet.readString("itemCode"),
				fieldSet.readString("manufacturerName"),
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
				fieldSet.readString("createdBy"),
				fieldSet.readDate("createdOn")
		);
	}
//	private Long parseLong(String value){
//		return value != null && !value.isEmpty() ? Long.parseLong(value) : null;
//	}
}
