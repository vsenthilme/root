package com.mnrclara.api.management.model.clientgeneral;

import java.util.Date;

public interface IClientGeneralNew {

	public String getClientId();
	public String getClassId();
	public String getFirstNameLastName();
	public String getCorporationClientId();
	public String getEmailId();
	public String getContactNumber();
	public String getAddressLine1();
	public String getAddressLine2();
	public String getIntakeFormNumber();
	public Long getIntakeFormId();
	public String getCity();
	public String getState();
	public String getZipCode();
	public String getStatusId();

	public String getCreatedOnString();
}
