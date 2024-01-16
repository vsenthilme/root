package com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2;

import com.tekclover.wms.api.transaction.model.outbound.ordermangement.SearchOrderManagementLine;
import lombok.Data;

import java.util.List;

@Data
public class SearchOrderManagementLineV2 extends SearchOrderManagementLine {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;

}