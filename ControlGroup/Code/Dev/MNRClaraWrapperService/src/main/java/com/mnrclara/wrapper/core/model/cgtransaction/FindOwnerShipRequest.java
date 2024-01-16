package com.mnrclara.wrapper.core.model.cgtransaction;

import lombok.Data;
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
    private List<String> createdBy;
}
