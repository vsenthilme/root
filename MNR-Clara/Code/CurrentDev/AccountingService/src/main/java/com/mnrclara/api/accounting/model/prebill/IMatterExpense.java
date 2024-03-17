package com.mnrclara.api.accounting.model.prebill;

import java.util.Date;

public interface IMatterExpense {
	
	public Long getMatterExpenseId();
	public String getExpenseCode();
	public String getLanguageId();
	public Long getClassId();
	public String getMatterNumber();
	public String getCaseInformationNo();
	public String getClientId();	
	public Long getCaseCategoryId();
	public Long getCaseSubCategoryId();
	public Double getCostPerItem();
	public Double getNumberofItems();
	public Double getExpenseAmount();
	public String getRateUnit();
	public String getExpenseDescription();
	public String getExpenseType();
	public String getBillType();
	public Boolean getWriteOff();
	public String getExpenseAccountNumber();
	public Long getStatusId();
	public String getCreatedBy();
    public Date getCreatedOn();
    public String getUpdatedBy();
	public Date getUpdatedOn();
}
