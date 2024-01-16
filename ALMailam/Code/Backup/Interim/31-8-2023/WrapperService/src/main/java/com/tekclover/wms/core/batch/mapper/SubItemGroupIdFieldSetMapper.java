package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.SubItemGroupId;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class SubItemGroupIdFieldSetMapper implements FieldSetMapper<SubItemGroupId> {

    @Override
    public SubItemGroupId mapFieldSet(FieldSet fieldSet) {
        return new SubItemGroupId(
                fieldSet.readString("companyCodeId"),
                fieldSet.readString("plantId"),
                fieldSet.readString("warehouseId"),
                fieldSet.readLong("itemTypeId"),
                fieldSet.readLong("itemGroupId"),
                fieldSet.readLong("subItemGroupId"),
                fieldSet.readString("languageId"),
                fieldSet.readString("subItemGroup"),
                fieldSet.readString("description"),
                fieldSet.readString("companyIdAndDescription"),
                fieldSet.readString("plantIdAndDescription"),
                fieldSet.readString("warehouseIdAndDescription"),
                fieldSet.readString("itemTypeIdAndDescription"),
                fieldSet.readString("itemGroupIdAndDescription"),
                fieldSet.readLong("deletionIndicator"),
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
                fieldSet.readString("createdBy"));
    }
}
