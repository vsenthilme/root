package com.mnrclara.api.cg.transaction.model.ownershiprequest;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindOwnerShipRequest {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> requestId;
    private List<Long> groupTypeId;
    private List<String> storeId;
    private List<Long> subGroupId;
    private List<Long> groupId;
    private List<Long> statusId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
    private List<String> createdBy;

}
