package com.mnrclara.api.setup.model.clientuser;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblclientuser")
public class ClientUser { 
	
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Id
	@Column(name = "CLIENT_USR_ID") 
	private String clientUserId;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "CLIENT_CAT_ID") 
	private Long clientCategoryNo;
	
	@Column(name = "CONT_NO") 
	private String contactNumber;
	
	@Column(name = "FIRST_NM")
	private String firstName;
	
	@Column(name = "LAST_NM") 
	private String lastName;
	
	@Column(name = "FIRST_LAST_NM")
	private String firstNameLastName;
	
	@Column(name = "STATUS_ID") 
	private String statusId;
	
	@Column(name = "EMAIL_ID") 
	private String emailId;
	
	@Column(name = "QUOTATION") 
	private Integer quotation;
	
	@Column(name = "PAYMENTPLAN")
	private Integer paymentPlan;
	
	@Column(name = "MATTER") 
	private Integer matter;
	
	@Column(name = "DOCUMENT") 
	private Integer documents;
	
	@Column(name = "INVOICE") 
	private Integer invoice;
	
	@Column(name = "AGREEMENT") 
	private Integer agreement;
	
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
	
	@Column(name = "CTD_BY")
	private String createdBy;
	
	@Column(name = "CTD_ON")
	private Date createdOn = new Date();
	
	@Column(name = "UTD_BY") 
	private String updatedBy;
	
	@Column(name = "UTD_ON") 
	private Date updatedOn = new Date();
}
