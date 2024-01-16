package com.tekclover.wms.api.transaction.model.inbound.staging;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `STG_NO`
 */
@Table(
		name = "tblstagingheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_stagingheader", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_IB_NO", "REF_DOC_NO", "STG_NO"})
				}
		)
@IdClass(StagingHeaderCompositeKey.class)
public class StagingHeader { 
	
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
	
	@Column(name = "IB_ORD_TYP_ID")
	protected Long inboundOrderTypeId;
	
	@Column(name = "STATUS_ID") 
	protected Long statusId;
	
	@Column(name = "CONT_REC_NO") 
	protected String containerReceiptNo;
	
	@Column(name = "DOCK_ALL_NO") 
	protected String dockAllocationNo;
	
	@Column(name = "CONT_NO")
	protected String containerNo;
	
	@Column(name = "VEH_NO") 
	protected String vechicleNo;
	
	@Column(name = "GR_MTD") 
	protected String grMtd;
	
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
	
	@Column(name = "ST_CTD_BY") 
	protected String createdBy;
	
	@Column(name = "ST_CTD_ON") 
	protected Date createdOn = new Date();
	
	@Column(name = "ST_UTD_BY") 
	protected String updatedBy;
	
	@Column(name = "ST_UTD_ON") 
	protected Date updatedOn = new Date();
	
	@Column(name = "ST_CNF_BY") 
	protected String confirmedBy;
	
	@Column(name = "ST_CNF_ON") 
	protected Date confirmedOn = new Date();
}
