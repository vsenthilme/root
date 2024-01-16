package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterExpense;

public class MatterExpenseFieldSetMapper implements FieldSetMapper<MatterExpense> {

	@Override
	public MatterExpense mapFieldSet(FieldSet fieldSet) {
		return new MatterExpense(
			fieldSet.readLong("matterExpenseId"),
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("expenseCode"),
			fieldSet.readDouble("expenseAmount"),
			fieldSet.readString("expenseDescription"),
			fieldSet.readString("expenseType"),
			fieldSet.readString("billType"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("referenceField2")
		);
	}
}