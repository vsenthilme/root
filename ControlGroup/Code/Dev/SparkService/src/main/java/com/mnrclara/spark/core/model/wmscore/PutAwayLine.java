package com.mnrclara.spark.core.model.wmscore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data

public class PutAwayLine { 

	private String languageId;

	private String companyCode;

	private String plantId;

	private String warehouseId;

	private String goodsReceiptNo;

	private String preInboundNo;

	private String refDocNumber;

	private String putAwayNumber;

	private Long lineNo;

	private String itemCode;

	private String proposedStorageBin;

	private String confirmedStorageBin;

	private String packBarcodes;

	private Double putAwayQuantity;

	private String putAwayUom;

	private Double putawayConfirmedQty;

	private Long variantCode;

	private String variantSubCode;

	private String storageMethod;

	private String batchSerialNumber;

	private Long inboundOrderTypeId;

	private Long stockTypeId;

	private Long specialStockIndicatorId;

	private String referenceOrderNo;

	private Long statusId;

	private String description;

	private String specificationActual;

	private String vendorCode;

	private String manufacturerPartNo;

	private String hsnCode;

	private String itemBarcode;

	private Date manufacturerDate;

	private Date expiryDate;

	private Double storageQty;

	private String storageTemperature;

	private String storageUom;

	private String quantityType;

	private String proposedHandlingEquipment;

	private String assignedUserId;

	private Long workCenterId;

	private String putAwayHandlingEquipment;

	private String putAwayEmployeeId;

	private String companyDescription;

	private String plantDescription;

	private String warehouseDescription;

	private String statusDescription;

	private String createRemarks;

	private String cnfRemarks;

	private String referenceField1;

	private String referenceField2;

	private String referenceField3;

	private String referenceField4;

	private String referenceField5;

	private String referenceField6;

	private String referenceField7;

	private String referenceField8;

	private String referenceField9;

	private String referenceField10;

	private Long deletionIndicator;

	private String createdBy;

	private Timestamp createdOn;

	private String confirmedBy;

	private Timestamp confirmedOn;

	private String updatedBy;

	private Timestamp updatedOn;
}
