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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `STG_NO`, `PAL_CODE`, `CASE_CODE`, `IB_LINE_NO`, `ITM_CODE`
 */
@Table(
		name = "tblstagingline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_stagingline", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_IB_NO", "REF_DOC_NO", "STG_NO", "PAL_CODE", "CASE_CODE", "IB_LINE_NO", "ITM_CODE"})
				}
		)
@IdClass(StagingLineCompositeKey.class)
public class StagingLineEntity { 
	
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
	@Column(name = "PAL_CODE") 
	protected String palletCode;
	
	@Id
	@Column(name = "CASE_CODE") 
	protected String caseCode;
	
	@Id
	@Column(name = "IB_LINE_NO")
	protected Long lineNo;
	
	@Id
	@Column(name = "ITM_CODE") 
	protected String itemCode;
	
	@Column(name = "IB_ORD_TYP_ID") 
	protected Long inboundOrderTypeId;
	
	@Column(name = "VAR_ID") 
	protected Long variantCode;
	
	@Column(name = "VAR_SUB_ID") 
	protected String variantSubCode;
	
	@Column(name = "STR_NO") 
	protected String batchSerialNumber;
	
	@Column(name = "STCK_TYP_ID")
	protected Long stockTypeId;
	
	@Column(name = "SP_ST_IND_ID") 
	protected Long specialStockIndicatorId;
	
	@Column(name = "ST_MTD") 
	protected String storageMethod;
	
	@Column(name = "STATUS_ID")
	protected Long statusId;
	
	@Column(name = "PARTNER_CODE")	
	protected String businessPartnerCode;
	
	@Column(name = "CONT_NO") 
	protected String containerNo;
	
	@Column(name = "INV_NO") 
	protected String invoiceNo;
	
	@Column(name = "ORD_QTY")	
	protected Double orderQty;
	
	@Column(name = "ORD_UOM") 
	protected String orderUom;
	
	@Column(name = "ITM_PAL_QTY")
	protected Double itemQtyPerPallet;
	
	@Column(name = "ITM_CASE_QTY")
	protected Double itemQtyPerCase;
	
	@Column(name = "ASS_USER_ID") 
	protected String assignedUserId;
	
	@Column(name = "ITEM_TEXT") 
	protected String itemDescription;
	
	@Column(name = "MFR_PART") 
	protected String manufacturerPartNo;
	
	@Column(name = "HSN_CODE") 
	protected String hsnCode;
	
	@Column(name = "VAR_TYP") 
	protected String variantType;
	
	@Column(name = "SPEC_ACTUAL") 
	protected String specificationActual;
	
	@Column(name = "ITM_BARCODE")
	protected String itemBarcode;
	
	@Column(name = "REF_ORD_NO")
	protected String referenceOrderNo;
	
	@Column(name = "REF_ORD_QTY")
	protected Double referenceOrderQty;
	
	@Column(name = "CROSS_DOCK_ALLOC_QTY") 
	protected Double crossDockAllocationQty;
	
	@Column(name = "REMARK") 
	protected String remarks;
	
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
	
	@Column(name = "IS_DELETED") 
	protected Long deletionIndicator;
	
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
