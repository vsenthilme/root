package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrderLine;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OutboundOrderLineV2 extends OutboundOrderLine {

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

    private String manufacturerName;
    private String fulfilmentMethod;

    private Long middlewareId;
    private Long middlewareHeaderId;
    private String middlewareTable;
    private String supplierInvoiceNo;
}