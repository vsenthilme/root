package com.mnrclara.api.crm.model.pcitform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblfeedbackformid")
public class FeedbackForm {

	@Id
	@Column(name = "IT_FORM_NO", columnDefinition = "nvarchar(255)")
	private String intakeFormNumber;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "LANG_ID", columnDefinition = "nvarchar(20)")
	private String languageId;
	
	@Column(name = "INQ_NO", columnDefinition = "nvarchar(100)")
	private String inquiryNumber;
	
	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "OVR_ALL_EXP", columnDefinition = "nvarchar(100)")
	private String overallExperience;

	@Column(name = "INQ_EXP", columnDefinition = "nvarchar(100)")
	private String inquiryExperience;

	@Column(name = "FST_PSN_INTERACTION", columnDefinition = "nvarchar(100)")
	private String firstPersonInteraction;

	@Column(name = "CONSULTATION_SCHEDULE", columnDefinition = "nvarchar(100)")
	private String consultationSchedule;

	@Column(name = "STF_FLW_UP", columnDefinition = "nvarchar(100)")
	private String staffFollowUp;

	@Column(name = "ATT_CONSULTATION", columnDefinition = "nvarchar(100)")
	private String attorneyConsultation;

	@Column(name = "CONSULTATION_INFORMATIVE", columnDefinition = "nvarchar(100)")
	private String consultationInformative;

	@Column(name = "ATT_DISCUSSION_COMFORT", columnDefinition = "nvarchar(100)")
	private String attorneyDiscussionComfortable;

	@Column(name = "REFER_FRIEND_FAMILY", columnDefinition = "nvarchar(100)")
	private String referFriendOrFamily;

	@Column(name = "IMPROVE_REMARK", columnDefinition = "nvarchar(1000)")
	private String improveRemark;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1", columnDefinition = "nvarchar(100)")
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2", columnDefinition = "nvarchar(100)")
	private String referenceField2;
	
	@Column(name = "REF_FIELD_3", columnDefinition = "nvarchar(100)")
	private String referenceField3;
	
	@Column(name = "REF_FIELD_4", columnDefinition = "nvarchar(100)")
	private String referenceField4;
	
	@Column(name = "REF_FIELD_5", columnDefinition = "nvarchar(100)")
	private String referenceField5;
	
	@Column(name = "REF_FIELD_6", columnDefinition = "nvarchar(100)")
	private String referenceField6;
	
	@Column(name = "REF_FIELD_7", columnDefinition = "nvarchar(100)")
	private String referenceField7;
	
	@Column(name = "REF_FIELD_8", columnDefinition = "nvarchar(100)")
	private String referenceField8;
	
	@Column(name = "REF_FIELD_9", columnDefinition = "nvarchar(100)")
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10", columnDefinition = "nvarchar(100)")
	private String referenceField10;
	
	@Column(name = "CTD_BY", columnDefinition = "nvarchar(100)")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn;

	@Column(name = "UTD_BY", columnDefinition = "nvarchar(100)")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
