package com.tekclover.wms.api.transaction.model.outbound.outboundreversal.v2;

import com.tekclover.wms.api.transaction.model.outbound.SearchOutboundReversal;
import lombok.Data;

import java.util.List;

@Data
public class SearchOutboundReversalV2 extends SearchOutboundReversal {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;

}