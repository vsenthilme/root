package com.tekclover.wms.api.transaction.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BinClassId { 
	
	private String companyCodeId;
	private String plantId;	
	private String warehouseId;
	private Long binClassId;	
	private String languageId;
	private String binClass;	
	private Long deletionIndicator;	
	private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
	private Date updatedOn = new Date();	
}
