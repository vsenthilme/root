package com.mnrclara.wrapper.core.model.datamigration;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class MatterTimeTicket {
	
	@CsvBindByPosition(position = 0) 
	private String languageId;
	
	@CsvBindByPosition(position = 1) 
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 3) 
	private String clientId;
	
	@CsvBindByPosition(position = 4) 
	private Long caseCategoryId;
	
	@CsvBindByPosition(position = 5) 
	private Long caseSubCategoryId;
	
	@CsvBindByPosition(position = 6) 
	private String timeTicketNumber;
	
	@CsvBindByPosition(position = 7)
	private Double timeTicketHours;
	
	@CsvBindByPosition(position = 8) 
	private String timeTicketDate;
	
	@CsvBindByPosition(position = 9) 
	private Double timeTicketAmount;
	
	@CsvBindByPosition(position = 10)	
	private String billType;
	
	@CsvBindByPosition(position = 11)
	private String timeTicketDescription;
	
	@CsvBindByPosition(position = 12) 
	private Long statusId;
	
	@CsvBindByPosition(position = 13) 
	private Long deletionIndicator;
	
	@CsvBindByPosition(position = 14)
	private String createdBy;
	
	@CsvBindByPosition(position = 15) 
	private String createdOn;
}
