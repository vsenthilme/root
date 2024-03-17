package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

public interface InvoiceHeaderInterface { 
	
	public String getInvoiceNumber();
	
	public String getLanguageId();
	
	public Long getClassId();
	
	public String getMatterNumber();
	
	public String getClientId();
	
	public String getInvoiceFiscalYear();
	
	public String getInvoicePeriod();
	
	public Date getPostingDate();
	
	public String getReferenceText();
	
	public Long getCaseCategoryId();
	
	public Long getCaseSubCategoryId();	
	
	public String getPartnerAssigned();
	
	public Date getInvoiceDate();
	
	public Double getTotalBillableHours();
	
	public Double getInvoiceAmount();
	
	public String getCurrency();
	
	public String getArAccountNumber();
	
/////////////////////////////////////
	
	public String getPaymentPlanNumber();
	
	public Date getBillStartDate();
	
	public Date getCostCutDate();
	
	public Date getPaymentCutDate();
	
	public Date getTrustCutoffDate();
	
	public Double getTotalPaidAmount();
	
//	@Column(name = "REMAIN_BAL") 
//	public Double remainingBalance();
//	
//	@Column(name = "STATUS_ID") 
//	public Long statusId();
//	
//	@Column(name = "IS_DELETED") 
//	public Long deletionIndicator();
//	
//	@Column(name = "REF_FIELD_1") 
//	public String referenceField1();
//	
//	@Column(name = "REF_FIELD_2") 
//	public String referenceField2();
//	
//	@Column(name = "REF_FIELD_3") 
//	public String referenceField3();
//	
//	@Column(name = "REF_FIELD_4") 
//	public String referenceField4();
//	
//	@Column(name = "REF_FIELD_5") 
//	public String referenceField5();
//	
//	@Column(name = "REF_FIELD_6")
//	public String referenceField6();
//	
//	@Column(name = "REF_FIELD_7") 
//	public String referenceField7();
//	
//	@Column(name = "REF_FIELD_8") 
//	public String referenceField8();
//	
//	@Column(name = "REF_FIELD_9") 
//	public String referenceField9();
//	
//	@Column(name = "REF_FIELD_10") 
//	public String referenceField10();
//	
//	@Column(name = "CTD_BY")
//	public String createdBy();
//	
//	@Column(name = "CTD_ON")
//	public Date createdOn = new Date()();
//	
//	@Column(name = "UTD_BY")
//	public String updatedBy();
//	
//	@Column(name = "UTD_ON")
//	public Date updatedOn = new Date()();

}
