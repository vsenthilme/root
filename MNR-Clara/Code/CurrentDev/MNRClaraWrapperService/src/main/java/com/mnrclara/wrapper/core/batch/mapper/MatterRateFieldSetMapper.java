package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterRate;

public class MatterRateFieldSetMapper implements FieldSetMapper<MatterRate> {
	/*
	 * LANG_ID	CLASS_ID	CLIENT_ID	MATTER_NO	CASEINFO_NO	TK_CODE	DEF_RATE	ASSIGNED_RATE	
	 * RATE_UNIT	STATUS_ID	Isdeleted	CTD_BY	CTD_ON
	 */
	@Override
	public MatterRate mapFieldSet(FieldSet fieldSet) {
		return new MatterRate(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("caseInformationNo"),
			fieldSet.readString("timeKeeperCode"),
			fieldSet.readDouble("defaultRatePerHour"),
			fieldSet.readDouble("assignedRatePerHour"),
			fieldSet.readString("rateUnit"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn")
		);
	}
}