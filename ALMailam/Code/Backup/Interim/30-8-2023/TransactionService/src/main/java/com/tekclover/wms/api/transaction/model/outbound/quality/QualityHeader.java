package com.tekclover.wms.api.transaction.model.outbound.quality;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_OB_NO`, `REF_DOC_NO`, `PARTNER_CODE`, 
 * `PU_NO`, `QC_NO`, `PICK_HE_NO`
 */
@Table(
		name = "tblqualityheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_qualityheader", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_OB_NO", "REF_DOC_NO", 
								"PARTNER_CODE", "PU_NO", "QC_NO", "PICK_HE_NO"})
				}
		)
@IdClass(QualityHeaderCompositeKey.class)
public class QualityHeader { 
	
	@Id
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Id
	@Column(name = "C_ID") 
	private String companyCodeId;
	
	@Id
	@Column(name = "PLANT_ID")
	private String plantId;
	
	@Id
	@Column(name = "WH_ID") 
	private String warehouseId;
	
	@Id
	@Column(name = "PRE_OB_NO")
	private String preOutboundNo;
	
	@Id
	@Column(name = "REF_DOC_NO") 
	private String refDocNumber;
	
	@Id
	@Column(name = "PARTNER_CODE")
	private String partnerCode;
	
	@Id
	@Column(name = "PU_NO")
	private String pickupNumber;
	
	@Id
	@Column(name = "QC_NO") 
	private String qualityInspectionNo;
	
	@Id
	@Column(name = "PICK_HE_NO")
	private String actualHeNo;
	
	@Column(name = "OB_ORD_TYP_ID") 
	private Long outboundOrderTypeId;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "QC_TO_QTY") 
	private String qcToQty;
	
	@Column(name = "QC_UOM")
	private String qcUom;
	
	@Column(name = "MFR_PART")
	private String manufacturerPartNo;
	
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
	
	@Column(name = "REMARK") 
	private String remarks;
	
	@Column(name = "QC_CTD_BY")
	private String qualityCreatedBy;
	
	@Column(name = "QC_CTD_ON")
	private Date qualityCreatedOn = new Date();
	
	@Column(name = "QC_CNF_BY") 
	private String qualityConfirmedBy;
	
	@Column(name = "QC_CNF_ON") 
	private Date qualityConfirmedOn = new Date();
	
	@Column(name = "QC_UTD_BY")
	private String qualityUpdatedBy;
	
	@Column(name = "QC_UTD_ON")
	private Date qualityUpdatedOn = new Date();
	
	@Column(name = "QC_REV_BY") 
	private String qualityReversedBy;
	
	@Column(name = "QC_REV_ON") 
	private Date qualityReversedOn = new Date();
}
