package com.almailem.ams.api.connector.model.wms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class InterWarehouseTransferOutHeader {

    private String fromWhsID;                            // WH_ID

    private String toWhsID;                            // PARTNER_CODE

    private String storeName;                            // PARTNER_NM

    private String companyCode;

    @Column(nullable = false)
    @NotBlank(message = "From Company Code is mandatory")
    private String fromCompanyCode;

    @Column(nullable = false)
    @NotBlank(message = "To Company Code is mandatory")
    private String toCompanyCode;

    @Column(nullable = false)
    @NotBlank(message = "From Branch Code is mandatory")
    private String fromBranchCode;

    @Column(nullable = false)
    @NotBlank(message = "To Branch Code is mandatory")
    private String toBranchCode;

    @Column(nullable = false)
    @NotBlank(message = "Transfer Order Number is mandatory")
    private String transferOrderNumber;

    @Column(nullable = false)
    @NotBlank(message = "Required Delivery Date is mandatory")
    private String requiredDeliveryDate;

    private String isCompleted;
    private Date updatedOn;

    private String branchCode;
    private String languageId;
    private String orderType;

    @JsonIgnore
    private String id;

    @JsonIgnore
    private Date orderReceivedOn;

    @JsonIgnore
    private Long statusId;

    //MiddleWare Fields
    private Long middlewareId;
    private String middlewareTable;
}
