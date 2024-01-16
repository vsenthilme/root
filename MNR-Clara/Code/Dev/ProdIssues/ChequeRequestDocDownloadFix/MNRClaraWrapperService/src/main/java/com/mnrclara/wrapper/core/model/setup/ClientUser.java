package com.mnrclara.wrapper.core.model.setup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
public class ClientUser { 
	
	private String languageId;
	private Long classId;
	private String clientUserId;
	private String clientId;
	private Long clientCategoryNo;
	private String contactNumber;
	private String firstName;
	private String lastName;
	private String firstNameLastName;
	private String statusId;
	private String emailId;
	private Integer quotation;
	private Integer paymentPlan;
	private Integer matter;
	private Integer documents;
	private Integer invoice;
	private Integer agreement;
	private Long deletionIndicator;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
