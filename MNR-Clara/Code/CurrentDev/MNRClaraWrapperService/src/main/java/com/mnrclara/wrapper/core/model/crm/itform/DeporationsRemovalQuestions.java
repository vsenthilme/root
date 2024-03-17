package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class DeporationsRemovalQuestions {

	private boolean haveYouEverbeenOrAreYouNowInImmigrationProceedings;
	private boolean haveYouEverbeenDetainedAndDidNotGoToCourt;
	private boolean haveYouEverbeenDeportedAndReturnedUnlawfully;
	private Date approximateDate;
	
	private boolean haveYouEverbeenOrderedDeportedAndNeverLeft;
	private boolean haveYouEverbeenGrantedVoluntaryDeportationAndNeverLeft;
	private boolean haveYouEverAppliedForAnyKindOfReliefFromDeportation;
}
