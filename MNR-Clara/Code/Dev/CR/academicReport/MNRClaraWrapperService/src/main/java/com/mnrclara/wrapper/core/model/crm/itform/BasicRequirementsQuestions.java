package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class BasicRequirementsQuestions {

	private boolean haveYouBeenAPermanentResidentFor5OR3Years;
	private boolean areYouLivingAtTheSameAddressForPast3Months;
	private boolean wereAnyOfYourParentsORGrandparentsUSCitizensBefore18thBirthday;
	private boolean haveYouRegisteredWithSelectiveService;
	private String selectiveServiceSystem;
	private Date registeredDate;
	private String notes;
}
