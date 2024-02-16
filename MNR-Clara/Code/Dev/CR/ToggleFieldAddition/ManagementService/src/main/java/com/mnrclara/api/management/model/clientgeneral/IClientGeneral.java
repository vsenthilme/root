package com.mnrclara.api.management.model.clientgeneral;

import java.util.Date;

public interface IClientGeneral {

	public String getClientId();
	public String getLanguageId();
	public Long getClassId();
	public Long getClientCategoryId();
	public Long getTransactionId();
	public String getPotentialClientId();
	public String getInquiryNumber();
	public Long getIntakeFormId();
	public String getIntakeFormNumber();
	public String getFirstName();
	public String getLastName();
	public String getFirstNameLastName();
	public String getLastNameFirstName();
	public String getCorporationClientId();
	public Long getReferralId();
	public String getEmailId();
	public String getContactNumber();
	public String getAddressLine1();
	public String getAddressLine2();
	public String getAddressLine3();
	public String getCity();
	public String getState();
	public String getCountry();
	public String getZipCode();
	public String getConsultationDate();
	public String getSocialSecurityNo();
	public String getMailingAddress();
	public String getOccupation();
	public Long getStatusId();
	public String getSuiteDoorNo();
	public String getWorkNo();
	public String getHomeNo();
	public String getFax();
	public String getAternateEmailId();
	public Boolean getIsMailingAddressSame();
	public String getReferenceField29();
	public String getReferenceField30();
	public String getCreatedBy();
	public Date getCreatedOn();
	public String getUpdatedBy();
	public Date getUpdatedOn();
}
