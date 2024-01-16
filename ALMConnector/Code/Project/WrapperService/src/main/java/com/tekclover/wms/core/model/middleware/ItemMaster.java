package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ItemMaster {

    private Long itemMasterId;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    @NotBlank(message = "Item Description is mandatory")
    private String itemDescription;

    @NotBlank(message = "Unit Of Measure is mandatory")
    private String uom;

    private Long itemGroupId;

    private Long secondaryItemGroupId;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotBlank(message = "Manufacturer Short Name is mandatory")
    private String manufacturerShortName;

    private String manufacturerFullName;

    @NotBlank(message = "Created Username is mandatory")
    private String createdUsername;

    @NotBlank(message = "Item Creation Date is mandatory")
    private Date itemCreationDate;

    @NotBlank(message = "Is_New is mandatory")
    private String isNew;
}
