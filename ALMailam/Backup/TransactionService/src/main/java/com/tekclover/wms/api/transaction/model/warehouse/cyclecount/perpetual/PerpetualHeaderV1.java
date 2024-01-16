package com.tekclover.wms.api.transaction.model.warehouse.cyclecount.perpetual;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class PerpetualHeaderV1 {

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "CycleCountNo is mandatory")
    private String cycleCountNo;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    private String branchName;

    private Date cycleCountCreationDate;

//    @NotBlank(message = "Is-New is mandatory")
    private String isNew;

    private String isCompleted;
    private String isCancelled;
    private Date updatedOn;

    //MiddleWare Fields
    private Long middlewareId;
    private String middlewareTable;

}
