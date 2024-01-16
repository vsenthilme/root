package com.mnrclara.api.management.model.matterdoclist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(	name = "tblmatterdoclistline")
@Where(clause = "IS_DELETED=0")
public class MatterDocListLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MATTER_LINE_ID")
	private long matterLineId;
	@Column(name = "MATTER_HEADER_ID")
	private long matterHeaderId;
	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CHK_LIST_NO")
	private Long checkListNo;

	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "DOCUMENT_URL")
	private String documentUrl;

	@Column(name = "CLIENT_ID")
	private String clientId;

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
	
	@Column(name = "CTD_BY")
	private String createdBy;
	
	@Column(name = "CTD_ON") 
	private Date createdOn = new Date();
	
	@Column(name = "UTD_BY") 
	private String updatedBy;
	
	@Column(name = "UTD_ON") 
	private Date updatedOn = new Date();
}
