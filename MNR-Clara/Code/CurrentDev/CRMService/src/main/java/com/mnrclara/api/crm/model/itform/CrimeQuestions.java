package com.mnrclara.api.crm.model.itform;

import lombok.Data;

@Data
public class CrimeQuestions {

	private boolean haveYouEverbeenCitedTrafficTickets;
	private boolean haveYouEverbeenArrestedByPolice;
	private boolean haveYouEverbeenChargedWithViolatingAnyLawOrCrimeOrOffense;
	private boolean haveYouEverbeenInJail;
	private boolean areYouCurrentlyOnProbationOrParole;
	private boolean haveYouFailedToRevealApplicableArrestsWhenApplyingForResidency;
	private boolean haveYouLiedOrCommittedFraudToReceivePublicBenefits;
	private boolean haveYouMarriedSomeoneInOrderToObtainAnImmigrationBenefit;
}
