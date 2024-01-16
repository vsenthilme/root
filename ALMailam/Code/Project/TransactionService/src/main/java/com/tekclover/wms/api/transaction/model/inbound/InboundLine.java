package com.tekclover.wms.api.transaction.model.inbound;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `REF_DOC_NO`, `PRE_IB_NO`, `IB_LINE_NO`, `ITM_CODE`
 */
@Table(
		name = "tblinboundline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_inboundline", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "REF_DOC_NO", "PRE_IB_NO", "IB_LINE_NO", "ITM_CODE"})
				}
		)
@IdClass(InboundLineCompositeKey.class)
public class InboundLine { 
	
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
	@Column(name = "REF_DOC_NO") 
	protected String refDocNumber;
	
	@Id
	@Column(name = "PRE_IB_NO")
	protected String preInboundNo;
	
	@Id
	@Column(name = "IB_LINE_NO") 
	protected Long lineNo;
	
	@Id
	@Column(name = "ITM_CODE") 
	protected String itemCode;
	
	@Column(name = "ORD_QTY")
	protected Double orderedQuantity;
	
	@Column(name = "ORD_UOM") 
	protected String orderedUnitOfMeasure;
	
	@Column(name = "ACCEPT_QTY") 
	protected Double acceptedQty;
	
	@Column(name = "DAMAGE_QTY") 
	protected Double damageQty;
	
	@Column(name = "PA_CNF_QTY") 
	protected Double putawayConfirmedQty;
	
	@Column(name = "VAR_QTY") 
	protected Double varianceQty;
	
	@Column(name = "VAR_ID") 
	protected Long variantCode;
	
	@Column(name = "VAR_SUB_ID") 
	protected String variantSubCode;
	
	@Column(name = "IB_ORD_TYP_ID") 
	protected Long inboundOrderTypeId;
	
	@Column(name = "STCK_TYP_ID")
	protected Long stockTypeId;
	
	@Column(name = "SP_ST_IND_ID") 
	protected Long specialStockIndicatorId;
	
	@Column(name = "REF_ORD_NO") 
	protected String referenceOrderNo;
	
	@Column(name = "STATUS_ID") 
	protected Long statusId;
	
	@Column(name = "PARTNER_CODE")
	protected String vendorCode;
	
	@Column(name = "EA_DATE") 
	protected Date expectedArrivalDate;
	
	@Column(name = "CONT_NO") 
	protected String containerNo;
	
	@Column(name = "INV_NO") 
	protected String invoiceNo;
	
	@Column(name = "TEXT") 
	protected String description;
	
	@Column(name = "MFR_PART")
	protected String manufacturerPartNo;
	
	@Column(name = "HSN_CODE")
	protected String hsnCode;
	
	@Column(name = "ITM_BARCODE")
	protected String itemBarcode;
	
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
	
	@Column(name = "IS_DELETED") 
	protected Long deletionIndicator;
	
	@Column(name = "CTD_BY")
	protected String createdBy;
	
	@Column(name = "CTD_ON")
	protected Date createdOn = new Date();
	
	@Column(name = "UTD_BY")
	protected String updatedBy;
	
	@Column(name = "UTD_ON")
	protected Date updatedOn;
	
	@Column(name = "IB_CNF_BY")
	protected String confirmedBy;
	
	@Column(name = "IB_CNF_ON")
	protected Date confirmedOn;
}
