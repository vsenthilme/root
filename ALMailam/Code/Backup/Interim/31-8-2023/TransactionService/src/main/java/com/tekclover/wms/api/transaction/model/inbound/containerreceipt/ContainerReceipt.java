package com.tekclover.wms.api.transaction.model.inbound.containerreceipt;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `CONT_REC_NO`
 */
@Table(
		name = "tblcontainerreceipt", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_containerreceipt", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "CONT_REC_NO"})
				}
		)
@IdClass(ContainerReceiptCompositeKey.class)
public class ContainerReceipt { 
	
	@Id
	@Column(name = "LANG_ID") 
	protected String languageId;
	
	@Id
	@Column(name = "C_ID")
	protected String companyCodeId;
	
	@Id
	@Column(name = "PLANT_ID") 
	protected String plantId;
	
	@Id
	@Column(name = "WH_ID") 
	protected String warehouseId;
	
	@Column(name = "PRE_IB_NO") 
	protected String preInboundNo;
	
	@Column(name = "REF_DOC_NO")
	protected String refDocNumber;
	
	@Id
	@Column(name = "CONT_REC_NO")
	protected String containerReceiptNo;
	
	@Column(name = "CONT_REC_DATE")
	protected Date containerReceivedDate;
	
	@Column(name = "CONT_NO") 
	protected String containerNo;
	
	@Column(name = "STATUS_ID") 
	protected Long statusId;
	
	@Column(name = "CONT_TYP") 
	protected String containerType;
	
	@Column(name = "PARTNER_CODE")
	protected String partnerCode;
	
	@Column(name = "INV_NO") 
	protected String invoiceNo;
	
	@Column(name = "CONS_TYPE") 
	protected String consignmentType;
	
	@Column(name = "ORIGIN") 
	protected String origin;
	
	@Column(name = "PAL_QTY") 
	protected String numberOfPallets;
	
	@Column(name = "CASE_NO")
	protected String numberOfCases;
	
	@Column(name = "DOCK_ALL_NO") 
	protected String dockAllocationNo;
	
	@Column(name = "REMARK") 
	protected String remarks;
	
	@Column(name = "IS_DELETED") 
	protected Long deletionIndicator;
	
	// Unloaded by
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
	
	@Column(name = "CTD_BY")
	protected String createdBy;
	
	@Column(name = "CTD_ON")
	protected Date createdOn = new Date();
	
	@Column(name = "UTD_BY")
	protected String updatedBy;
	
	@Column(name = "UTD_ON")
	protected Date updatedOn = new Date();
}
