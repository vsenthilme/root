package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterAssignment;

public class MatterAssignmentFieldSetMapper implements FieldSetMapper<MatterAssignment> {
	@Override
	public MatterAssignment mapFieldSet(FieldSet fieldSet) {
		return new MatterAssignment(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("caseInformationNo"),
			fieldSet.readString("clientId"),
			fieldSet.readDate("caseOpenedDate"),
			fieldSet.readString("partner"),
			fieldSet.readString("originatingTimeKeeper"),
			fieldSet.readString("responsibleTimeKeeper"),
			fieldSet.readString("assignedTimeKeeper"),
			fieldSet.readString("legalAssistant"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn")
		);
	}
}