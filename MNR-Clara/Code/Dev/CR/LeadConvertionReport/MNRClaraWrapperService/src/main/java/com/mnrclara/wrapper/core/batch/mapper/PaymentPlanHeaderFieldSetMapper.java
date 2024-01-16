package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.PaymentPlanHeader;

public class PaymentPlanHeaderFieldSetMapper implements FieldSetMapper<PaymentPlanHeader> {

	@Override
	public PaymentPlanHeader mapFieldSet(FieldSet fieldSet) {
		return new PaymentPlanHeader(
			fieldSet.readString("paymentPlanNumber"),
			fieldSet.readLong("paymentPlanRevisionNo"),
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
			fieldSet.readDate("paymentPlanDate"),
			fieldSet.readString("quotationNo"),
			fieldSet.readDate("paymentPlanStartDate"),
			fieldSet.readDate("endDate"),
			fieldSet.readLong("noOfInstallment"),
			fieldSet.readDouble("paymentPlanTotalAmount"),
			fieldSet.readDouble("dueAmount"),
			fieldSet.readDouble("installmentAmount"),
			fieldSet.readString("currency"),
			fieldSet.readLong("paymentCalculationDayDate"),
			fieldSet.readString("paymentPlanText"),
			fieldSet.readDate("sentOn"),
			fieldSet.readDate("approvedOn"),
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