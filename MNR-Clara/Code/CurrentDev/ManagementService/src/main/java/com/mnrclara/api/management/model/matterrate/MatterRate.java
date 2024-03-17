package com.mnrclara.api.management.model.matterrate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name = "tblmatterrateid", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_matterrate", 
						columnNames = { "MATTER_NO", "TK_CODE"})
				}
		)
@IdClass(MatterRateCompositeKey.class)
public class MatterRate { 
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID") 
	private Long classId;	
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Id
	@Column(name = "TK_CODE") 
	private String timeKeeperCode;
	
	@Column(name = "CASEINFO_NO") 
	private String caseInformationNo;
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;
	
	@Column(name = "DEF_RATE") 
	private Double defaultRatePerHour;
	
	@Column(name = "ASSIGNED_RATE") 
	private Double assignedRatePerHour;
	
	@Column(name = "RATE_UNIT") 
	private String rateUnit;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1") 
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2") 
	private String referenceField2;
	
	@Column(name = "REF_FIELD_3") 
	private String referenceField3;
	
	@Column(name = "REF_FIELD_4") 
	private String referenceField4;
	
	@Column(name = "REF_FIELD_5") 
	private String referenceField5;
	
	@Column(name = "REF_FIELD_6") 
	private String referenceField6;
	
	@Column(name = "REF_FIELD_7") 
	private String referenceField7;
	
	@Column(name = "REF_FIELD_8") 
	private String referenceField8;
	
	@Column(name = "REF_FIELD_9") 
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10") 
	private String referenceField10;
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
