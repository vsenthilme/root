package com.tekclover.wms.api.transaction.model.inbound.preinbound;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PRE_IB_NO`, `REF_DOC_NO`, `IB_LINE_NO`, `ITM_CODE`
 */
@Table(
		name = "tblpreinboundline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_preinboundline", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PRE_IB_NO", "REF_DOC_NO", "IB_LINE_NO", "ITM_CODE"})
				}
		)
@IdClass(PreInboundLineCompositeKey.class)
public class PreInboundLineEntity { 
	
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
	
	@Column(name = "STATUS_ID")
	protected Long statusId;
	
	@Column(name = "ITEM_TEXT") 
	protected String itemDescription;
	
	@Column(name = "CONT_NO")
	protected String containerNo;
	
	@Column(name = "INV_NO")
	protected String invoiceNo;
	
	@Column(name = "PARTNER_CODE") 
	protected String businessPartnerCode;
	
	@Column(name = "PARTNER_ITM_CODE")
	protected String partnerItemNo;
	
	@Column(name = "BRND_NM") 
	protected String brandName;
	
	@Column(name = "MFR_PART")
	protected String manufacturerPartNo;
	
	@Column(name = "HSN_CODE")
	protected String hsnCode;
	
	@Column(name = "EA_DATE")
	protected Date expectedArrivalDate;
	
	@Column(name = "ORD_QTY")
	protected Double orderQty;
	
	@Column(name = "ORD_UOM")
	protected String orderUom;
	
	@Column(name = "STCK_TYP_ID") 
	protected Long stockTypeId;
	
	@Column(name = "SP_ST_IND_ID")
	protected Long specialStockIndicatorId;
	
	@Column(name = "PAL_QTY")
	protected String numberOfPallets;
	
	@Column(name = "CASE_NO") 
	protected String numberOfCases;
	
	@Column(name = "ITM_PAL_QTY") 
	protected Double itemPerPalletQty;
	
	@Column(name = "ITM_CASE_QTY")
	protected Double itemCaseQty; // PACK_QTY in AX_API
	
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
	
	@Column(name = "REF_FIELD_11") 
	protected String referenceField11;
	
	@Column(name = "REF_FIELD_12")
	protected String referenceField12;
	
	@Column(name = "REF_FIELD_13")
	protected String referenceField13;
	
	@Column(name = "REF_FIELD_14")
	protected String referenceField14;
	
	@Column(name = "REF_FIELD_15") 
	protected String referenceField15;
	
	@Column(name = "REF_FIELD_16") 
	protected String referenceField16;
	
	@Column(name = "REF_FIELD_17") 
	protected String referenceField17;
	
	@Column(name = "REF_FIELD_18") 
	protected String referenceField18;
	
	@Column(name = "REF_FIELD_19") 
	protected String referenceField19;
	
	@Column(name = "REF_FIELD_20") 
	protected String referenceField20;
	
	@Column(name = "IS_DELETED") 
	protected Long deletionIndicator;
	
	@Column(name = "CTD_BY")
	protected String createdBy;
	
	@Column(name = "CTD_ON")
	protected Date createdOn;
	
	@Column(name = "UTD_BY")
	protected String updatedBy;
	
	@Column(name = "UTD_ON")
	protected Date updatedOn;
}
