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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `GR_NO`, `PAL_CODE`, `CASE_CODE`, `PACK_BARCODE`, `IB_LINE_NO`, `ITM_CODE`
 */
@Table(
		name = "tblgrline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_grline", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_IB_NO", "REF_DOC_NO", 
								"GR_NO", "PAL_CODE", "CASE_CODE", "PACK_BARCODE", "IB_LINE_NO", "ITM_CODE", "GR_CTD_ON"})
				}
		)
@IdClass(GrLineCompositeKey.class)
public class GrLine { 
	
	@Id
	@Column(name = "LANG_ID", columnDefinition = "nvarchar(25)")
	protected String languageId;
	
	@Id
	@Column(name = "C_ID", columnDefinition = "nvarchar(25)")
	protected String companyCode;
	
	@Id
	@Column(name = "PLANT_ID", columnDefinition = "nvarchar(25)")
	protected String plantId;
	
	@Id
	@Column(name = "WH_ID", columnDefinition = "nvarchar(25)")
	protected String warehouseId;
	
	@Id
	@Column(name = "PRE_IB_NO", columnDefinition = "nvarchar(100)")
	protected String preInboundNo;
	
	@Id
	@Column(name = "REF_DOC_NO", columnDefinition = "nvarchar(100)")
	protected String refDocNumber;
	
	@Id
	@Column(name = "GR_NO", columnDefinition = "nvarchar(100)")
	protected String goodsReceiptNo;
	
	@Id
	@Column(name = "PAL_CODE", columnDefinition = "nvarchar(100)")
	protected String palletCode;
	
	@Id
	@Column(name = "CASE_CODE", columnDefinition = "nvarchar(100)")
	protected String caseCode;
	
	@Id
	@Column(name = "PACK_BARCODE", columnDefinition = "nvarchar(100)")
	protected String packBarcodes;
	
	@Id
	@Column(name = "IB_LINE_NO") 
	protected Long lineNo;
	
	@Id
	@Column(name = "ITM_CODE", columnDefinition = "nvarchar(100)")
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
	
	@Column(name = "ORD_QTY") 
	protected Double orderQty;
	
	@Column(name = "ORD_UOM")
	protected String orderUom;
	
	@Column(name = "GR_QTY") 
	protected Double goodReceiptQty;
	
	@Column(name = "GR_UOM") 
	protected String grUom;
	
	@Column(name = "ACCEPT_QTY") 
	protected Double acceptedQty;
	
	@Column(name = "DAMAGE_QTY") 
	protected Double damageQty;
	
	@Column(name = "QTY_TYPE") 
	protected String quantityType;
	
	@Column(name = "ASS_USER_ID") 
	protected String assignedUserId;
	
	@Column(name = "PAWAY_HE_NO") 
	protected String putAwayHandlingEquipment;
	
	@Column(name = "PA_CNF_QTY") 
	protected Double confirmedQty;
	
	@Column(name = "REM_QTY")
	protected Double remainingQty;
	
	@Column(name = "REF_ORD_NO")
	protected String referenceOrderNo;
	
	@Column(name = "REF_ORD_QTY") 
	protected Double referenceOrderQty;
	
	@Column(name = "CROSS_DOCK_ALLOC_QTY")
	protected Double crossDockAllocationQty;
	
	@Column(name = "MFR_DATE")
	protected Date manufacturerDate;
	
	@Column(name = "EXP_DATE") 
	protected Date expiryDate;
	
	@Column(name = "STR_QTY")
	protected Double storageQty;
	
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
	
	@Column(name = "GR_CTD_BY") 
	protected String createdBy;
	
	@Id
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
