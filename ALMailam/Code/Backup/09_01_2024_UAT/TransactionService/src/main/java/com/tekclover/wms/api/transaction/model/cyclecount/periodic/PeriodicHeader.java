package com.tekclover.wms.api.transaction.model.cyclecount.periodic;

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
 * `C_ID`, `PLANT_ID`, `WH_ID`, `CC_TYP_ID`, `CC_NO`
 */
@Table(
		name = "tblperiodicheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_periodicheader", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "CC_TYP_ID", "CC_NO"})
				}
		)
@IdClass(PeriodicHeaderCompositeKey.class)
public class PeriodicHeader { 
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Id
	@Column(name = "C_ID")
	private String companyCode;
	
	@Id
	@Column(name = "PLANT_ID") 
	private String plantId;
	
	@Id
	@Column(name = "WH_ID") 
	private String warehouseId;
	
	@Id
	@Column(name = "CC_TYP_ID")
	private Long cycleCountTypeId;
	
	@Id
	@Column(name = "CC_NO") 
	private String cycleCountNo;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
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
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator;
	
	@Column(name = "CC_CTD_BY")
	private String createdBy;
	
	@Column(name = "CC_CTD_ON")
	private Date createdOn = new Date();
	
	@Column(name = "CC_CNT_BY")
	private String countedBy;
	
	@Column(name = "CC_CNT_ON") 
	private Date countedOn = new Date();
	
	@Column(name = "CC_CNF_BY") 
	private String confirmedBy;
	
	@Column(name = "CC_CNF_ON") 
	private Date confirmedOn = new Date();
}
