package com.mnrclara.wrapper.core.model.datamigration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatterNote { 
	
	@CsvBindByPosition(position = 0) 
	private String languageId;
	
	@CsvBindByPosition(position = 1) 
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 3) 
	private String clientId;
	
	@CsvBindByPosition(position = 4) 
	private String notesNumber;
	
	@CsvBindByPosition(position = 5) 
	private String notesDescription;
	
	@CsvBindByPosition(position = 6) 
	private Long statusId;
	
	@CsvBindByPosition(position = 7) 
	private Long noteTypeId;
	
	@CsvBindByPosition(position = 8) 
	private Long deletionIndicator;
	
	@CsvBindByPosition(position = 9) 
	private String createdBy;
	
	@CsvBindByPosition(position = 10) 
	private String createdOn;
}
