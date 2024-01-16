package com.mnrclara.api.setup.model.expensecode;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "tblexpensecodeid")
public class ExpenseCode { 
	
	@Id
	@Column(name = "EXP_CODE")
	private String expenseCode;
	
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "COST_TYP")
	private String costType;
	
	@Column(name = "COST_ITEM")
	private Double costPerItem;
	
	@Column(name = "EXP_CODE_TEXT")
	private String expenseCodeDescription;
	
	@Column(name = "RATE_UNIT")
	private String rateUnit;
	
	@Column(name = "EXP_CODE_STATUS")
	private String expenseCodeStatus;
	
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
