package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.ClientNote;

public class ClientNoteFieldSetMapper implements FieldSetMapper<ClientNote> {

	@Override
	public ClientNote mapFieldSet(FieldSet fieldSet) {
		return new ClientNote(
			fieldSet.readString("notesNumber"),
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readString("noteText"),
			fieldSet.readString("matterNumber"),
			fieldSet.readLong("noteTypeId"),
			fieldSet.readLong("statusId"),
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
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("updatedBy"),
			fieldSet.readDate("updatedOn"),
			fieldSet.readString("additionalFields1"),
			fieldSet.readString("additionalFields2"),
			fieldSet.readString("additionalFields3"),
			fieldSet.readString("additionalFields4"),
			fieldSet.readString("additionalFields5"),
			fieldSet.readString("additionalFields6"),
			fieldSet.readString("additionalFields7")
		);
	}
}