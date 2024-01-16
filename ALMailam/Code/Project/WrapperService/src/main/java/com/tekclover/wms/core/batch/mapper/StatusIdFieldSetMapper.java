package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.StatusId;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class StatusIdFieldSetMapper implements FieldSetMapper<StatusId> {

    @Override
    public StatusId mapFieldSet(FieldSet fieldSet) {
        return new StatusId(
                fieldSet.readString("languageId"),
                fieldSet.readLong("statusId"),
                fieldSet.readString("status"),
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
