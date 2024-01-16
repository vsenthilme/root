package com.mnrclara.wrapper.core.model.datamigration;

import java.util.Date;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class ClientNote {
	
	@CsvBindByPosition(position = 0)
	private String languageId;
	
	@CsvBindByPosition(position = 1)
	private Long classId;
	
	@CsvBindByPosition(position = 2)
	private String clientId;
	
	@CsvBindByPosition(position = 3)
	private String notesNumber;
	
	@CsvBindByPosition(position = 4)
	private String noteText;
		
	@CsvBindByPosition(position = 5)
	private String matterNumber;
	
	@CsvBindByPosition(position = 6)
	private Long noteTypeId;
	
	@CsvBindByPosition(position = 7)
	private Long statusId;
	
	@CsvBindByPosition(position = 8)
	private String createdBy;
	
	@CsvBindByPosition(position = 9)
	private String createdOn;
	
	@CsvBindByPosition(position = 10)
	private String updatedBy;
	
	@CsvBindByPosition(position = 11)
	private String updatedOn;
	
	/*-------------------ADD_FIELDS------------------------------*/
	@CsvBindByPosition(position = 12)
	private String additionalFields1;
	
	@CsvBindByPosition(position = 13)
	private String additionalFields2;
	
	@CsvBindByPosition(position = 14)
	private String additionalFields3;
	
	@CsvBindByPosition(position = 15)
	private String additionalFields4;
	
	@CsvBindByPosition(position = 16)
	private String additionalFields5;
	
	@CsvBindByPosition(position = 17)
	private String additionalFields6;
	
	@CsvBindByPosition(position = 18)
	private String additionalFields7;
}
