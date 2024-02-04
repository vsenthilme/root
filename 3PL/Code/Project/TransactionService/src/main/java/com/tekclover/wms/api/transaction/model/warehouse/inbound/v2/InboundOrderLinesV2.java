package com.tekclover.wms.api.transaction.model.warehouse.inbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrderLines;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@ToString(callSuper = true)
public class InboundOrderLinesV2 extends InboundOrderLines {

    private String manufacturerCode;
    private String origin;
    private String supplierName;
    private String brand;
    private Double packQty;
    private String fromCompanyCode;
    private Double expectedQty;
    protected String storeID;

    private String sourceBranchCode;
    private String countryOfOrigin;
    private String manufacturerFullName;
    private String purchaseOrderNumber;

    //MiddleWare Fields
    private Long middlewareId;
    private Long middlewareHeaderId;
    private String middlewareTable;

    private String transferOrderNumber;
    private Date receivedDate;
    private Double receivedQty;
    private String receivedBy;
    private String isCompleted;
    private String isCancelled;
    private String supplierInvoiceNo;
    private String companyCode;
    private String branchCode;
}