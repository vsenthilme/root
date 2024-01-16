package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchOutboundReversalV2 extends SearchOutboundReversal {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;

}