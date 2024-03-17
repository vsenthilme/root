package com.mnrclara.api.crm.model.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientUSAImmigrationInfo {

	// Entered
	private Date enteredDate1;
	private Date enteredDate2;
	private Date enteredDate3;
	private Date enteredDate4;
	private Date enteredDate5;
	private Date lastEnteredDate;
	
	// Exited
	private Date exitedDate1;
	private Date exitedDate2;
	private Date exitedDate3;
	private Date exitedDate4;
	private Date exitedDate5;
	private Date lastExitedDate;
	


}
