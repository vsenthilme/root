package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class OutboundOrderV2 extends OutboundOrder {

    private String branchCode;
    private String companyCode;
    private String languageId;
    private String returnOrderReference;

    private String pickListNumber;
    private String pickListStatus;
    private String companyName;
    private String branchName;
    private String warehouseName;
    private String salesOrderNumber;
    private Date salesInvoiceDate;
    private String salesInvoiceNumber;
    private String sourceCompanyCode;
    private String targetCompanyCode;
    private String targetBranchCode;

    private String deliveryType;
    private String customerId;
    private String customerName;
    private String address;
    private String phoneNumber;
    private String alternateNo;
    private String status;

    private Long middlewareId;
    private String middlewareTable;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderId", fetch = FetchType.EAGER)
    private Set<OutboundOrderLineV2> line;
}


