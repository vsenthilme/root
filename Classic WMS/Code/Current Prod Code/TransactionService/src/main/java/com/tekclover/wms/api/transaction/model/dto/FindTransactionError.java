package com.tekclover.wms.api.transaction.model.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class FindTransactionError {

    private List<Long> errorId;

    private List<String> tableName;

    private List<String> transaction;

    private List<String> errorType;

    private List<String> createdBy;

    private Date startCreatedOn;

    private Date endCreatedOn;


}
