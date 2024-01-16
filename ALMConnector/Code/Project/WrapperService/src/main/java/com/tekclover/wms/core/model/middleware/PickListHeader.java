package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class PickListHeader {

    private Long pickListHeaderId;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "SalesOrderNumber is mandatory")
    private String salesOrderNumber;

    @NotBlank(message = "PickListNumber is mandatory")
    private String pickListNumber;

    @NotBlank(message = "PickListDate is mandatory")
    private Date pickListdate;

    private Set<PickListLine> pickListLines;
}
