package com.tekclover.wms.api.transaction.model.inbound;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `REF_DOC_NO`, `PRE_IB_NO`
 */
@Table(
		name = "tblinboundheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_inboundheader", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "REF_DOC_NO", "PRE_IB_NO"})
				}
		)
@IdClass(InboundHeaderCompositeKey.class)
public class InboundHeader { 
	
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
	@Column(name = "REF_DOC_NO") 
	private String refDocNumber;
	
	@Id
	@Column(name = "PRE_IB_NO") 
	private String preInboundNo;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IB_ORD_TYP_ID") 
	private Long inboundOrderTypeId;
	
	@Column(name = "CONT_NO") 
	private String containerNo;
	
	@Column(name = "VEH_NO") 
	private String vechicleNo;
	
	@Column(name = "IB_TEXT")
	private String headerText;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator;

	@Column(name = "COMPANY_DESC")
	private String companyDescription;

	@Column(name = "PLANT_DESC")
	private String plantDescription;

	@Column(name = "WAREHOUSE_DESC")
	private String warehouseDescription;

	@Column(name = "STATUS_DESC")
	private String statusDescription;

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
	private Date updatedOn;
	
	@Column(name = "IB_CNF_BY")
	private String confirmedBy;
	
	@Column(name = "IB_CNF_ON")
	private Date confirmedOn;
}
