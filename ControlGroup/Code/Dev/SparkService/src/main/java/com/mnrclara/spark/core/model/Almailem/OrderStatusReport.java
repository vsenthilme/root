package com.mnrclara.spark.core.model.Almailem;

import lombok.Data;
import org.apache.hadoop.yarn.util.Times;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class OrderStatusReport {

    private String languageId;          // LANG_ID
    private String companyCodeId;       // C_ID
    private String plantId;             // PLANT_ID
    private String warehouseId;			// WH_ID
    private Timestamp deliveryConfirmedOn;	// DLV_CNF_ON
    private String soNumber;			// REF_DOC_NO
    private String doNumber;			// DLV_ORD_NO
    private String customerCode;		// PARTNER_CODE
    private String customerName;		// PARTNER_NM
    private String sku;					// ITM_CODE
    private String skuDescription;		// ITEM_TEXT
    private Double orderedQty;			// ORD_QTY
    private Double deliveredQty;		// DLV_QTY
    private Timestamp orderReceivedDate;		// REF_DOC_DATE
    private Timestamp expectedDeliveryDate;	// REQ_DEL_DATE
    private Double percentageOfDelivered;	// (DLV_QTY/ORD_QTY)*100
    private String statusId;				// STATUS_ID
    private String orderType;				// ORDER_TYPE
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;

    private String itemCode;
    private String refDocNumber;
}
