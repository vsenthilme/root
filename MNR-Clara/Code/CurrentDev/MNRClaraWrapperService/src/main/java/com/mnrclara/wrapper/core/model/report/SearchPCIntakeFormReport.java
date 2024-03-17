package com.mnrclara.wrapper.core.model.report;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPCIntakeFormReport {
	
	/*
	 * CLASS_ID
	 * REF_FIELD_4
	 * REF_FIELD_2
	 * STATUS_ID
	 * CTD_ON
	 */
	private Long classId;
	private List<String> inquiryAssignedToRefField4; 	// Inquiry Assigned to
	private List<String> consultingAttorneyRefField2; 			// Consulting Attorney
	private List<Long> statusId;
	private Date fromCreatedOn;
	private Date toCreatedOn;
}
