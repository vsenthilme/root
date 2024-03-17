package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.InvoiceHeader;

public class InvoiceHeaderFieldSetMapper implements FieldSetMapper<InvoiceHeader> {

	@Override
	public InvoiceHeader mapFieldSet(FieldSet fieldSet) {
		return new InvoiceHeader(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
			fieldSet.readString("invoiceNumber"),
			fieldSet.readString("invoiceFiscalYear"),
			fieldSet.readString("invoicePeriod"),
			fieldSet.readDate("postingDate"),
			fieldSet.readString("referenceText"),
			fieldSet.readString("partnerAssigned"),
			fieldSet.readDate("invoiceDate"),
			fieldSet.readDouble("invoiceAmount"),
			fieldSet.readDouble("totalPaidAmount"),
			fieldSet.readDouble("remainingBalance"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn")
		);
	}
}