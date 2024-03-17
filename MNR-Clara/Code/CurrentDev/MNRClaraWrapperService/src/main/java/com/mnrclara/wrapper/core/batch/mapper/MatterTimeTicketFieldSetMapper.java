package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterTimeTicket;

public class MatterTimeTicketFieldSetMapper implements FieldSetMapper<MatterTimeTicket> {

	@Override
	public MatterTimeTicket mapFieldSet(FieldSet fieldSet) {
		return new MatterTimeTicket(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
//			fieldSet.readString("timeKeeperCode"),
			fieldSet.readLong("caseCategoryId"),
			fieldSet.readLong("caseSubCategoryId"),
			fieldSet.readString("timeTicketNumber"),
			fieldSet.readDouble("timeTicketHours"),
			fieldSet.readDate("timeTicketDate"),
			fieldSet.readDouble("timeTicketAmount"),
			fieldSet.readString("billType"),
			fieldSet.readString("timeTicketDescription"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn")
		);
	}
}