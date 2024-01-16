package com.mnrclara.api.management.model.matterdoclist;

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
/*
 * `LANG_ID`, `CLASS_ID`, `CHK_LIST_NO`, `MATTER_NO`, `CLIENT_ID`
 */
@Table(
		name = "tblmatterdoclist", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_matterdoclist", 
						columnNames = {"LANG_ID", "CLASS_ID", "CHK_LIST_NO", "MATTER_NO", "CLIENT_ID", "SEQ_NO"})
				}
		)
@IdClass(MatterDocListCompositeKey.class)
public class MatterDocList { 
	
	@Id
	@Column(name = "LANG_ID") 
	private String languageId;
		
	@Id
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Id
	@Column(name = "CHK_LIST_NO") 
	private Long checkListNo;
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;	
	
	@Id
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Id
	@Column(name = "SEQ_NO")
	private Long sequenceNumber;
	
	@Column(name = "DOC_NAME")
	private String documentName;
	
	@Column(name = "MATTER_TEXT") 
	private String matterText;
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator;
	
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
	
	@Column(name = "RECEIVED_ON") 
	private Date receivedOn;
	
	@Column(name = "CTD_BY")
	private String createdBy;
	
	@Column(name = "CTD_ON") 
	private Date createdOn = new Date();
	
	@Column(name = "UTD_BY") 
	private String updatedBy;
	
	@Column(name = "UTD_ON") 
	private Date updatedOn = new Date();
}
