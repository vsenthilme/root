package com.tekclover.wms.api.transaction.model.inbound.putaway;

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
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `GR_NO`, `PRE_IB_NO`, `REF_DOC_NO`, `PA_NO`, `IB_LINE_NO`, `ITM_CODE`, `PROP_ST_BIN`, `CNF_ST_BIN`
 */
@Table(
		name = "tblputawayline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_putawayline", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "GR_NO", "PRE_IB_NO", "REF_DOC_NO", "PA_NO", "IB_LINE_NO", "ITM_CODE", "PROP_ST_BIN", "CNF_ST_BIN"})
				}
		)
@IdClass(PutAwayLineCompositeKey.class)
public class PutAwayLine { 
	
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
	@Column(name = "GR_NO") 
	protected String goodsReceiptNo;
	
	@Id
	@Column(name = "PRE_IB_NO") 
	protected String preInboundNo;
	
	@Id
	@Column(name = "REF_DOC_NO")
	protected String refDocNumber;
	
	@Id
	@Column(name = "PA_NO") 
	protected String putAwayNumber;
	
	@Id
	@Column(name = "IB_LINE_NO")
	protected Long lineNo;
	
	@Id
	@Column(name = "ITM_CODE")
	protected String itemCode;
	
	@Id
	@Column(name = "PROP_ST_BIN") 
	protected String proposedStorageBin;
	
	@Id
	@Column(name = "CNF_ST_BIN")
	protected String confirmedStorageBin;
	
	@Column(name = "PACK_BARCODE")
	protected String packBarcodes;
	
	@Column(name = "PA_QTY") 
	protected Double putAwayQuantity;
	
	@Column(name = "PA_UOM") 
	protected String putAwayUom;
	
	@Column(name = "PA_CNF_QTY") 
	protected Double putawayConfirmedQty;
	
	@Column(name = "VAR_ID")
	protected Long variantCode;
	
	@Column(name = "VAR_SUB_ID")
	protected String variantSubCode;
	
	@Column(name = "ST_MTD") 
	protected String storageMethod;
	
	@Column(name = "STR_NO")
	protected String batchSerialNumber;
	
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
	
	@Column(name = "TEXT") 
	protected String description;
	
	@Column(name = "SPEC_ACTUAL") 
	protected String specificationActual;
	
	@Column(name = "VEN_CODE") 
	protected String vendorCode;
	
	@Column(name = "MFR_PART") 
	protected String manufacturerPartNo;
	
	@Column(name = "HSN_CODE") 
	protected String hsnCode;
	
	@Column(name = "ITM_BARCODE") 
	protected String itemBarcode;
	
	@Column(name = "MFR_DATE") 
	protected Date manufacturerDate;
	
	@Column(name = "EXP_DATE") 
	protected Date expiryDate;
	
	@Column(name = "STR_QTY")
	protected Double storageQty;
	
	@Column(name = "ST_TEMP") 
	protected String storageTemperature;
	
	@Column(name = "ST_UOM")
	protected String storageUom;
	
	@Column(name = "QTY_TYPE") 
	protected String quantityType;
	
	@Column(name = "PROP_HE_NO") 
	protected String proposedHandlingEquipment;
	
	@Column(name = "ASS_USER_ID") 
	protected String assignedUserId;
	
	@Column(name = "WRK_CTR_ID") 
	protected Long workCenterId;
	
	@Column(name = "PAWAY_HE_NO") 
	protected String putAwayHandlingEquipment;
	
	@Column(name = "PAWAY_EMP_ID")
	protected String putAwayEmployeeId;
	
	@Column(name = "CREATE_REMARK") 
	protected String createRemarks;
	
	@Column(name = "CNF_REMARK")	
	protected String cnfRemarks;
	
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
	
	@Column(name = "PA_CTD_BY")
	protected String createdBy;
	
	@Column(name = "PA_CTD_ON") 
	protected Date createdOn = new Date();
	
	@Column(name = "PA_CNF_BY") 
	protected String confirmedBy;
	
	@Column(name = "PA_CNF_ON")
	protected Date confirmedOn = new Date();
	
	@Column(name = "PA_UTD_BY") 
	protected String updatedBy;
	
	@Column(name = "PA_UTD_ON") 
	protected Date updatedOn = new Date();
}
