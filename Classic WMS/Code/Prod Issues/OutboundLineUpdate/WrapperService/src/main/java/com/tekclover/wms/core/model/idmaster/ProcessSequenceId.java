package com.tekclover.wms.core.model.idmaster;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

	public class ProcessSequenceId { 

	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private Long processId;
	private Long subLevelId;
	private String languageId;
	private String processDescription;
	private String subProcessDescription;
	private Long deletionIndicator = 0L;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
