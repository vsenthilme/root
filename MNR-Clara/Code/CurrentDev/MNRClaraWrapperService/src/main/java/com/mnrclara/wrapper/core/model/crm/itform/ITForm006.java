package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm006 extends BaseITForm {
    
    private String name;
    private Date date;
    private String authorizedAgent;
    private String emailAddress;
    private ClientContactNumberInfo contactNumber;
    private ClientAddress address;
    private ClientAddress billingAddress;
    
	private String consulationFor;
	private String purposeOfVisit;

	private boolean doYouAlreadyHaveAnAttorney;
	private String nameOfAttorney;
	
	// For Accountant/Assistant/Attorney to Fill Out
	private String partnerInCharge;
	private String attorney;
	private String legalAssistant;
	
	private String typeOfCase;
	private int noOfEmployeesInCaseType;
	private String OthersInCaseType;
	
	// Billing General Information 
	private String billingType;
	private String nameOfThirdPartyBilling;
	private String authorizedAgentOfThirdPartyBilling;
	private Long telephone;
	private String addressOfThirdPartyBilling;
	
	private boolean agreementSigned;
	
	// HOW DID YOU HEAR ABOUT OUR FIRM
	private ClientReferenceMedium clientReferenceMedium;
	
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
}