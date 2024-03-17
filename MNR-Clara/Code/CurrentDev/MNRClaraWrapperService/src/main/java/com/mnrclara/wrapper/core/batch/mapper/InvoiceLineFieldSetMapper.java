package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.InvoiceLine;

public class InvoiceLineFieldSetMapper implements FieldSetMapper<InvoiceLine> {

	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param clientId
	 * @param invoiceNumber
	 * @param serialNumber
	 * @param invoiceFiscalYear
	 * @param invoicePeriod
	 * @param itemNumber
	 * @param invoicedetailDescription
	 * @param billableAmount
	 * @param statusId
	 * @param deletionIndicator
	 * @param createdBy
	 * @param createdOn
	 */
	@Override
	public InvoiceLine mapFieldSet(FieldSet fieldSet) {
		return new InvoiceLine(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("clientId"),
			fieldSet.readString("invoiceNumber"),
			fieldSet.readString("serialNumber"),
			fieldSet.readString("invoiceFiscalYear"),
			fieldSet.readString("invoicePeriod"),
			fieldSet.readLong("itemNumber"),
			fieldSet.readString("invoicedetailDescription"),
			fieldSet.readDouble("billableAmount"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn")
		);
	}
}