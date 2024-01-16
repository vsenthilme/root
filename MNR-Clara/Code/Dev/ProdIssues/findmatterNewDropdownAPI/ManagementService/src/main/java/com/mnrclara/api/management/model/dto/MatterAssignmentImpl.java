package com.mnrclara.api.management.model.dto;

import java.util.Date;
public interface MatterAssignmentImpl {

	public String getCaseInformationNo();
	public String getLanguageId();
	public Long getClassId();
	public String getMatterNumber();
	public String getMatterDescription();
	public String getClientId();
	public Long getCaseCategoryId();
	public Long getCaseSubCategoryId();
	public Date getCaseOpenedDate();
	public String getPartner();
	public String getOriginatingTimeKeeper();
	public String getResponsibleTimeKeeper();
	public String getAssignedTimeKeeper();
	public String getLegalAssistant();
	public Long getStatusId();
	public Long getDeletionIndicator();
	public String getReferenceField1();
	public String getReferenceField2();
	public String getReferenceField3();
	public String getReferenceField4();
	public String getReferenceField5();
	public String getReferenceField6();
	public String getReferenceField7();
	public String getReferenceField8();
	public String getReferenceField9();
	public String getReferenceField10();
	public String getCreatedBy();
	public Date getCreatedOn();
	public String getUpdatedBy();
	public Date getUpdatedOn();

	public String getClientName();
}
