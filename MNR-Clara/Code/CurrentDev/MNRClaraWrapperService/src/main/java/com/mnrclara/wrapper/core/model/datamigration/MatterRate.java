package com.mnrclara.wrapper.core.model.datamigration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatterRate { 
	
	@CsvBindByPosition(position = 0) 
	private String languageId;
	
	@CsvBindByPosition(position = 1) 
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private String clientId;
	
	@CsvBindByPosition(position = 3) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 4)
	private String caseInformationNo;
	
	@CsvBindByPosition(position = 5) 
	private String timeKeeperCode;
	
	@CsvBindByPosition(position = 6) 
	private Double defaultRatePerHour;
	
	@CsvBindByPosition(position = 7) 
	private Double assignedRatePerHour;
	
	@CsvBindByPosition(position = 8)
	private String rateUnit;
	
	@CsvBindByPosition(position = 9) 
	private Long statusId;
	
	@CsvBindByPosition(position = 10) 
	private Long deletionIndicator = 0L;
	
	@CsvBindByPosition(position = 11) 
	private String createdBy;
	
	@CsvBindByPosition(position = 12)
	private String createdOn;
}
