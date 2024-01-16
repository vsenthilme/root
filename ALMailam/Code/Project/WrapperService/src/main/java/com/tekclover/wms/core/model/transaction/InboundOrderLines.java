package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class InboundOrderLines {

    protected Long id;
    protected Long lineReference;                                // IB_LINE_NO
    protected String itemCode;                                // ITM_CODE
    protected String itemText;                                // ITEM_TEXT
    protected String invoiceNumber;                            // INV_NO
    protected String containerNumber;                        // CONT_NO
    protected String supplierCode;                            // PARTNER_CODE
    protected String supplierPartNumber;                        // PARTNER_ITM_CODE
    protected String manufacturerName;                        // BRND_NM
    protected String manufacturerPartNo;                        // MFR_PART
    protected Date expectedDate;                                // EA_DATE
    protected Double orderedQty;                                // ORD_QTY
    protected String uom;                                        // ORD_UOM
    protected Double itemCaseQty;                                // ITM_CASE_QTY
    protected String salesOrderReference;                        // REF_FIELD_4
    protected String orderId;
}
