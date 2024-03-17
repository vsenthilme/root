package com.tekclover.wms.api.transaction.model.errorlog;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindErrorLog {

    private List<String> orderTypeId;
    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<String> refDocNumber;

    private Date fromOrderDate;
    private Date toOrderDate;

}
