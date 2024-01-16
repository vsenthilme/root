package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.PaymentPlanLine;

public class PaymentPlanLineFieldSetMapper implements FieldSetMapper<PaymentPlanLine> {
	@Override
	public PaymentPlanLine mapFieldSet(FieldSet fieldSet) {
		return new PaymentPlanLine(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
			fieldSet.readString("paymentPlanNumber"),
			fieldSet.readLong("paymentPlanRevisionNo"),
			fieldSet.readLong("itemNumber"),
			fieldSet.readString("quotationNo"),
			fieldSet.readDate("installmentduedate"),
			fieldSet.readDate("dueDate"),
			fieldSet.readDouble("dueAmount"),
			fieldSet.readDouble("remainingDueNow"),
			fieldSet.readString("currency"),
			fieldSet.readLong("paymentReminderDays"),
			fieldSet.readDate("reminderDate"),
			fieldSet.readString("reminderStatus"),
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
			fieldSet.readString("createdBy")
		);
	}
}