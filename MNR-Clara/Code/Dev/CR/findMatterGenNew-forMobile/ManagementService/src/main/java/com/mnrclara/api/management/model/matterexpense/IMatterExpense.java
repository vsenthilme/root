package com.mnrclara.api.management.model.matterexpense;

import java.util.Date;

public interface IMatterExpense { 
	
	public Long getMatterExpenseId();
	public String getExpenseCode();
	public Date getCreatedOn();
	public String getCreatedBy();
	public String getExpenseDescription();
	public Double getNumberOfItems();
	public Double getExpenseAmount();
	public String getBillType();
	public Date getReferenceField2();
	public String getReferenceField3();
	public String getReferenceField4();
}
