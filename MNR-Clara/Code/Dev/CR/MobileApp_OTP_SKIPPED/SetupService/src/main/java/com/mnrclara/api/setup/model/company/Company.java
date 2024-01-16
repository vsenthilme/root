package com.mnrclara.api.setup.model.company;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)

/*
 * `COMP_ID`, `LANG_ID`
 */
@Table(
		name = "tblcompanyid", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_company", 
						columnNames = { "COMP_ID", "LANG_ID"})
				}
		)
@IdClass(CompanyCompositeKey.class)
public class Company { 
	
	@Id
	@Column(name = "COMP_ID")
	private String companyId;
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "COMP_TEXT")
	private String companyName;
	
	@Column(name = "COMP_ADDRESS_1")
	private String companyAddress1;
	
	@Column(name = "COMP_ADDRESS_2")
	private String companyAddress2;
	
	@Column(name = "CONTACT_NM")
	private String contactName;
	
	@Column(name = "CONTACT_NO")
	private String contactNumber;
	
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
