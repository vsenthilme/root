package com.mnrclara.api.crm.model.inquiry;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchInquiry {
	private List<String> inquiryNumber;
	private Date inqStartDate;
	private Date inqEndDate;
	private List<Long> inquiryModeId;
	private String firstName;
	private String lastName;
	private String email;
	private String contactNumber;
	private List<String> assignedUserId;
	private Date sAssignedOn;
	private Date eAssignedOn;
	private List<Long> statusId;
	private List<Long> classId;
}
