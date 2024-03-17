package com.mnrclara.wrapper.core.model.datamigration;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class MatterAssignment {

	@CsvBindByPosition(position = 0) 
	private String languageId;
	
	@CsvBindByPosition(position = 1)
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 3) 
	private String caseInformationNo;
	
	@CsvBindByPosition(position = 4) 
	private String clientId;
	
	@CsvBindByPosition(position = 5) 
	private Long caseCategoryId;
	
	@CsvBindByPosition(position = 6) 
	private Long caseSubCategoryId;
	
	@CsvBindByPosition(position = 7) 
	private String caseOpenedDate;
	
	@CsvBindByPosition(position = 8) 
	private String partner;
	
	@CsvBindByPosition(position = 9) 
	private String originatingTimeKeeper;
	
	@CsvBindByPosition(position = 10) 
	private String responsibleTimeKeeper;
	
	@CsvBindByPosition(position = 11) 
	private String assignedTimeKeeper;
	
	@CsvBindByPosition(position = 12)
	private String legalAssistant;
	
	@CsvBindByPosition(position = 13) 
	private Long statusId;
	
	@CsvBindByPosition(position = 14)
	private Long deletionIndicator;
	
	@CsvBindByPosition(position = 15) 
	private String createdBy;
	
	@CsvBindByPosition(position = 16) 
	private String createdOn;
}
