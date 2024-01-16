package com.tekclover.wms.api.transaction.model.inbound.gr;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `STG_NO`, `GR_NO`, `PAL_CODE`, `CASE_CODE`
 */
@Table(
		name = "tblgrheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_grheader", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_IB_NO", "REF_DOC_NO", "STG_NO", 
								"GR_NO", "PAL_CODE", "CASE_CODE"})
				}
		)
@IdClass(GrHeaderCompositeKey.class)
public class GrHeader { 
	
	@Id
	@Column(name = "LANG_ID")
	protected String languageId;
	
	@Id
	@Column(name = "C_ID") 
	protected String companyCode;
	
	@Id
	@Column(name = "PLANT_ID")
	protected String plantId;
	
	@Id
	@Column(name = "WH_ID") 
	protected String warehouseId;
	
	@Id
	@Column(name = "PRE_IB_NO") 
	protected String preInboundNo;
	
	@Id
	@Column(name = "REF_DOC_NO")
	protected String refDocNumber;
	
	@Id
	@Column(name = "STG_NO")
	protected String stagingNo;
	
	@Id
	@Column(name = "GR_NO") 
	protected String goodsReceiptNo;
	
	@Id
	@Column(name = "PAL_CODE") 
	protected String palletCode;
	
	@Id
	@Column(name = "CASE_CODE")
	protected String caseCode;
	
	@Column(name = "IB_ORD_TYP_ID")
	protected Long inboundOrderTypeId;
	
	@Column(name = "STATUS_ID") 
	protected Long statusId;
	
	@Column(name = "GR_MTD")
	protected String grMethod;
	
	@Column(name = "CONT_REC_NO") 
	protected String containerReceiptNo;
	
	@Column(name = "DOCK_ALL_NO") 
	protected String dockAllocationNo;
	
	@Column(name = "CONT_NO") 
	protected String containerNo;
	
	@Column(name = "VEH_NO") 
	protected String vechicleNo;
	
	@Column(name = "EA_DATE") 
	protected Date expectedArrivalDate;
	
	@Column(name = "GR_DATE") 
	protected Date goodsReceiptDate;
	
	@Column(name = "IS_DELETED") 
	protected Long deletionIndicator;
	
	@Column(name = "REF_FIELD_1") 
	protected String referenceField1;
	
	@Column(name = "REF_FIELD_2") 
	protected String referenceField2;
	
	@Column(name = "REF_FIELD_3") 
	protected String referenceField3;
	
	@Column(name = "REF_FIELD_4")
	protected String referenceField4;
	
	@Column(name = "REF_FIELD_5") 
	protected String referenceField5;
	
	@Column(name = "REF_FIELD_6") 
	protected String referenceField6;
	
	@Column(name = "REF_FIELD_7") 
	protected String referenceField7;
	
	@Column(name = "REF_FIELD_8") 
	protected String referenceField8;
	
	@Column(name = "REF_FIELD_9")
	protected String referenceField9;
	
	@Column(name = "REF_FIELD_10")
	protected String referenceField10;
	
	@Column(name = "GR_CTD_BY")
	protected String createdBy;
	
	@Column(name = "GR_CTD_ON") 
	protected Date createdOn = new Date();
	
	@Column(name = "GR_UTD_BY") 
	protected String updatedBy;
	
	@Column(name = "GR_UTD_ON")
	protected Date updatedOn = new Date();
	
	@Column(name = "GR_CNF_BY")
	protected String confirmedBy;
	
	@Column(name = "GR_CNF_ON") 
	protected Date confirmedOn = new Date();
}
