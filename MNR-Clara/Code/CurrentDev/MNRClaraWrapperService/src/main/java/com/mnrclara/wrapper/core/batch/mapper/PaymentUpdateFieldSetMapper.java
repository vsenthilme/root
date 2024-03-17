package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.PaymentUpdate;

public class PaymentUpdateFieldSetMapper implements FieldSetMapper<PaymentUpdate> {

	/**
	 * @Param paymentId
	 * @param invoiceNumber
	 * @param classId
	 * @param clientId
	 * @param createdBy
	 * @param createdOn
	 * @param languageId
	 * @param matterNumber
	 * @param paymentAmount
	 * @param paymentDate
	 * @param paymentNumber
	 * @param paymentText
	 * @param postingDate
	 * @param statusId
	 * @param transactionType
	 * @param paymentCode
	 */
	
	@Override
	public PaymentUpdate mapFieldSet(FieldSet fieldSet) {
		return new PaymentUpdate(
			fieldSet.readLong("paymentId"),
			fieldSet.readString("invoiceNumber"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("languageId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readDouble("paymentAmount"),
			fieldSet.readDate("paymentDate"),
			fieldSet.readString("paymentNumber"),
			fieldSet.readString("paymentText"),
			fieldSet.readDate("postingDate"),
			fieldSet.readString("statusId"),
			fieldSet.readString("transactionType"),
			fieldSet.readString("paymentCode")
		);
	}
}