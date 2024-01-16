package com.mnrclara.wrapper.core.model.crm.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientReceivedBenefits {

	// Have you ever requested and received any of the benefits listed FOR YOU?
	/*------------------------------------------*/
	private boolean anyHelpFromFederalStateOrLocalGovernment;
	private String sinceWhenHelpOfferedFromFederalStateOrLocalGovernment;
	
	/*------------------------------------------*/
	private boolean foodStamps;
	private String sinceWhenFoodStamps;
	
	/*------------------------------------------*/
	private boolean publicHousing;
	private String sinceWhenPublicHousing;
	
	/*------------------------------------------*/
	private boolean medicaid;
	private String sinceWhenMedicaid;
	
	// OTHER EVIDENCE
	/*------------------------------------------*/
	private String doYouOwnYourHome;
	private String whatType;
	
	/*------------------------------------------*/
	private String haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct;
	private Date dateOfCrimeOrViolentActCommitted;
	private String wasItReported;
	private String summaryOfWhatHappened;
}
