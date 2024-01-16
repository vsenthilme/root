package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmatterlnereport")
public class MatterLNEReport {

	@Id
	@Column(name = "MATTER_LNE_REPORT_ID")
	private Long matterLNEReportID;
	
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name = "MATTER_NO")
	private String matterNumber;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CASE_CAT_ID")
	private String caseCategoryId;
	
	@Column(name = "CASE_SUBCAT_ID")
	private String caseSubCategoryId;
	
	@Column(name = "MATTER_DESC")
	private String matterDescription;
	
	@Column(name = "BILL_MODE_TEXT")
	private String billModeText;
	
	@Column(name = "MATTER_OPENED_DATE")
	private Date matterOpenedDate;
	
	@Column(name = "MATTER_CLOSED_DATE")
	private Date matterClosedDate;
	
	@Column(name = "CTD_BY")
	private String createdBy;	
	
	@Column(name = "UPD_BY")
	private String updatedBy;	
	
	@Column(name = "REFERRED_BY")
	private String referredBy;	
	
	@Column(name = "CORPORATE_NAME")
	private String corporateName;
	
	@Column(name = "ORIGINATING_TIMEKEEPER")
	private String originatingTimeKeeper;
	
	@Column(name = "ASSIGNED_TIMEKEEPER")
	private String assignedTimeKeeper;
	
	@Column(name = "RESPONSIBLE_TIMEKEEPER")
	private String responsibleTimeKeeper;
	
	@Column(name = "NOTES_TEXT")
	private String notesText;
	
	// LNE
	@Column(name = "TYPE_OF_MATTER")
	private String typeOfMatter;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "LOCATION_OF_FILE")
	private String locationOfFile;
	
	@Column(name = "DEFENDANTS")
	private String defendants;
	
	@Column(name = "CAUSE_NO")
	private String causeNo;
	
	@Column(name = "PLAIN_TIFFS")
	private String plaintiffs;
	
	@Column(name = "ADVPARTY1_NAME")
	private String advParty1Name;
	
	@Column(name = "ADVPARTY1_EMAIL")
	private String advParty1Email;
	
	@Column(name = "ADVPARTY1_CELLPHONE")
	private String advParty1CellPhone;
	
	@Column(name = "ADVPARTY2_NAME")
	private String advParty2Name;
	
	@Column(name = "ADVPARTY2_EMAIL")
	private String advParty2Email;
	
	@Column(name = "ADVPARTY2_CELLPHONE")
	private String advParty2CellPhone;
	
	@Column(name = "JUDGE_NAME")
	private String judgeName;
	
	@Column(name = "EMAIL")
	private String email;			// Court Email
	
	@Column(name = "OFFICE_TELEPHONE")
	private String officeTelephone;	// Court Phone
	
	@Column(name = "AGENCY_NAME")
	private String agencyName;
	
	@Column(name = "AGENT_NAME")
	private String agentName;
	
	@Column(name = "AGENT_EMAIL")
	private String agentEmail;
	
	@Column(name = "AGENT_OFFICE_TELEPHONE")
	private String agentOfficeTelephone;
	
	@Column(name = "SCHEDULE_DATE")
	private Date scheduleDate;
	
	@Column(name = "TASKS_TODO")
	private String tasksToDo;
	
	@Column(name = "TASK_DATE")
	private Date taskDate;

	@Column(name = "PARTNER")
	private String partner;

	@Column(name = "LEGAL_ASSITANT")
	private String legalAssitant;

	@Column(name = "LAW_CLERK")
	private String lawClerk;
}
