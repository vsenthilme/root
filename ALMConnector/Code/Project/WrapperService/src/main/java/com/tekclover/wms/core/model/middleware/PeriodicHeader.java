package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class PeriodicHeader {

    private Long periodicHeaderId;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "CycleCount Number is mandatory")
    private String cycleCountNumber;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    private String branchName;

    @NotBlank(message = "CycleCount Creation Date is mandatory")
    private Date cycleCountCreationDate;

    @NotBlank(message = "Is_New is mandatory")
    private String isNew;

    private Set<PeriodicLine> periodicLines;
}
