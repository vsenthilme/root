package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

public interface IMatterGenAcc {

	public String getMatterNumber();
	public String getLanguageId();
	public Long getClassId();
	public String getCaseInformationNo();
	public String getClientId();
	public Long getTransactionId();
	public Long getCaseCategoryId();
	public Long getCaseSubCategoryId();
	public String getMatterDescription();
	public String getFileNumber();
	public Long getCaseFileNumber();
	public Date getCaseOpenedDate();
	public Date getCaseClosedDate();
	public Date getCaseFiledDate();
	public Date getPriorityDate();
	public String getReceiptNoticeNo();
	public Date getReceiptDate();
	public Date getExpirationDate();
	public Date getCourtDate();
	public Date getApprovalDate();
	public String getBillingModeId();
	public String getBillingFrequencyId();
	public String getBillingFormatId();
	public String getBillingRemarks();
	public String getArAccountNumber();
	public String getTrustDepositNo();
	public Double getFlatFeeAmount();
	public Double getAdministrativeCost();
	public Double getContigencyFeeAmount();
	public String getRateUnit();
	public String getDirectPhoneNumber();
	public Long getStatusId();
	public String getCreatedBy();
	public Date getCreatedOn();
	public String getUpdatedBy();
	public Date getUpdatedOn();
}
