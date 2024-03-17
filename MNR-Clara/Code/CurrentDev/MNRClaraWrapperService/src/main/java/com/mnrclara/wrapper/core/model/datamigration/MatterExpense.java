package com.mnrclara.wrapper.core.model.datamigration;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class MatterExpense { 
	
	@CsvBindByPosition(position = 0) 
	private String languageId;
	
	@CsvBindByPosition(position = 1) 
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private Long matterExpenseId;
	
	@CsvBindByPosition(position = 3) 
	private String clientId;
	
	@CsvBindByPosition(position = 4) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 5) 
	private String caseInformationNo;
	
	@CsvBindByPosition(position = 6) 
	private String expenseCode;
	
	@CsvBindByPosition(position = 7)
	private Double expenseAmount;
	
	@CsvBindByPosition(position = 8)
	private String expenseDescription;
	
	@CsvBindByPosition(position = 9) 
	private String expenseType;
	
	@CsvBindByPosition(position = 10) 
	private String billType;
	
	@CsvBindByPosition(position = 11) 
	private Long statusId;
	
	@CsvBindByPosition(position = 12) 
	private Long deletionIndicator = 0L;
	
	@CsvBindByPosition(position = 13) 
	private String createdBy;
	
	@CsvBindByPosition(position = 14)
	private String createdOn;
}
