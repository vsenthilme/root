package com.mnrclara.wrapper.core.batch.mapper;

import java.util.Date;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterGenAcc;

public class MatterGenAccFieldSetMapper implements FieldSetMapper<MatterGenAcc> {

	@Override
	public MatterGenAcc mapFieldSet(FieldSet fieldSet) {
		return new MatterGenAcc(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
			fieldSet.readString("caseInformationNo"),
			fieldSet.readLong("transactionId"),
			fieldSet.readLong("caseCategoryId"),
			fieldSet.readLong("caseSubCategoryId"),
			fieldSet.readString("billingModeId"),
			fieldSet.readString("billingFormatId"),
			fieldSet.readString("billingFrequencyId"),
			fieldSet.readString("billingRemarks"),
			fieldSet.readString("matterDescription"),
			fieldSet.readDate("caseOpenedDate"),
			fieldSet.readString("arAccountNumber"),
			fieldSet.readString("trustDepositNo"),
			fieldSet.readDouble("flatFeeAmount"),
			fieldSet.readDouble("administrativeCost"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("UpdatedBy"),
			fieldSet.readString("referenceField25")
		);
	}
}