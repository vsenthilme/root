package com.mnrclara.api.management.model.expirationdate;

import java.util.Date;

public interface IExpirationDate {

	/*
	 * CLIENT_ID, REMINDER_DATE , REMINDER_TEXT
	 */
	public String getClientId();
	public String getMatterNumber();
	public Date getReminderDate();
	public String getReminderText();
	public Boolean getToggleNotification();
	public String getClassId();
	public String getCorporationName();
	public String getClassName();
	public String getClientName();
	public String getClientEmail();
	public String getMatterDescription();
	public String getDocumentType();
	public Date getExpirationDate();
	public Double getReminderDays();
}
