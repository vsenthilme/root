package com.tekclover.wms.api.transaction.model.deliveryline;

import lombok.Data;
import java.util.List;

@Data
public class SearchDeliveryLine  {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<Long> deliveryNo;
    private List<String> itemCode;
    private List<Long> lineNumber;
    private List<String> refDocNumber;
    private List<String> invoiceNumber;
    private List<String> remarks;
    private List<Long> statusId;
}
