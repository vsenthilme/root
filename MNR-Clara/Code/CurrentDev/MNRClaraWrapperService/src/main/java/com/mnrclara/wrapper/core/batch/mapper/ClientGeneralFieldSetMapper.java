package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.ClientGeneral;

public class ClientGeneralFieldSetMapper implements FieldSetMapper<ClientGeneral> {

	@Override
	public ClientGeneral mapFieldSet(FieldSet fieldSet) {
		return new ClientGeneral(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readLong("clientCategoryId"),
			fieldSet.readLong("transactionId"),
			fieldSet.readString("potentialClientId"),
			fieldSet.readString("inquiryNumber"),
			fieldSet.readLong("intakeFormId"),
			fieldSet.readString("intakeFormNumber"),
			fieldSet.readString("firstName"),
			fieldSet.readString("lastName"),
			fieldSet.readString("firstNameLastName"),
			fieldSet.readString("lastNameFirstName"),
			fieldSet.readString("corporationClientId"),
			fieldSet.readString("referralId"),
			fieldSet.readString("emailId"),
			fieldSet.readString("contactNumber"),
			fieldSet.readString("addressLine1"),
			fieldSet.readString("addressLine2"),
			fieldSet.readString("addressLine3"),
			fieldSet.readString("city"),
			fieldSet.readString("state"),
			fieldSet.readString("country"),
			fieldSet.readString("zipCode"),
			fieldSet.readString("consultationDate"),
			fieldSet.readString("socialSecurityNo"),
			fieldSet.readString("mailingAddress"),
			fieldSet.readString("occupation"),
			fieldSet.readLong("statusId"),
			fieldSet.readString("suiteDoorNo"),
			fieldSet.readString("workNo"),
			fieldSet.readString("homeNo"),
			fieldSet.readString("fax"),
			fieldSet.readString("alternateEmailId"),
			fieldSet.readBoolean("isMailingAddressSame"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("referenceField21"),
			fieldSet.readString("referenceField22"),
			fieldSet.readString("referenceField23"),
			fieldSet.readString("referenceField25"),
			fieldSet.readString("referenceField26"),
			fieldSet.readString("referenceField27"),
			fieldSet.readString("referenceField28"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("UpdatedBy"),
			fieldSet.readDate("updatedOn")
		);
	}
}