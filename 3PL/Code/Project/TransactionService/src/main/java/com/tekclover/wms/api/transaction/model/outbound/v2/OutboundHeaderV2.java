package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.OutboundHeader;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
@ToString(callSuper = true)
public class OutboundHeaderV2 extends OutboundHeader {

    @Column(name = "INVOICE_NO", columnDefinition = "nvarchar(100)")
    private String invoiceNumber;

    @Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
    private String companyDescription;

    @Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
    private String plantDescription;

    @Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
    private String warehouseDescription;

    @Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
    private String statusDescription;


    @Column(name = "STATUS", columnDefinition = "nvarchar(100)")
    private String status;

    @Column(name = "PICK_LIST_NUMBER", columnDefinition = "nvarchar(150)")
    private String pickListNumber;

    @Column(name = "SALES_ORDER_NUMBER", columnDefinition = "nvarchar(150)")
    private String salesOrderNumber;

    /*-------------------------------------------------------------------------------------------------*/

    @Column(name = "PICK_LINE_COUNT")
    private String countOfPickedLine;

    @Column(name = "SUM_PICK_QTY")
    private String sumOfPickedQty;
}