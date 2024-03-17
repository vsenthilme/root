package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class ExpenseEntry {

	private Long matterExpenseId;
	private Date createdOn;
	private String expenseDescription;
	private Double numberofItems;
	private Double expenseAmount;
	private String billType;
	private String expenseCode;
}


