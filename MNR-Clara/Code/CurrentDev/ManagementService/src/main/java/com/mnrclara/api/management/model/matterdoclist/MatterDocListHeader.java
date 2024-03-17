package com.mnrclara.api.management.model.matterdoclist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmatterdoclistheader")
@Where(clause = "IS_DELETED=0")
public class MatterDocListHeader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MATTER_HEADER_ID")
	private Long matterHeaderId;

	@Column(name = "LANG_ID") 
	private String languageId;

	@Column(name = "CLASS_ID") 
	private Long classId;

	@Column(name = "CHK_LIST_NO") 
	private Long checkListNo;

	@Column(name = "MATTER_NO") 
	private String matterNumber;	

	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name = "MATTER_TEXT") 
	private String matterText;
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;
	@Column(name = "SENT_BY")
	private String sentBy;
	@Column(name = "SENT_DATE")
	private Date sentDate;
	@Column(name = "RECEIVED_DATE")
	private Date receivedDate;
	@Column(name = "RESENT_DATE")
	private Date resentDate;
	@Column(name = "APPROVED_DATE")
	private Date approvedDate;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "matterHeaderId",fetch = FetchType.EAGER)
	private Set<MatterDocListLine> matterDocLists;
	
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
