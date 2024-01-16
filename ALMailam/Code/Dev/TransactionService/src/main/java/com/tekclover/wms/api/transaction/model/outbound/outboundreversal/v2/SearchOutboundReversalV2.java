package com.tekclover.wms.api.transaction.model.outbound.outboundreversal.v2;

import com.tekclover.wms.api.transaction.model.outbound.SearchOutboundReversal;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class SearchOutboundReversalV2 extends SearchOutboundReversal {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;

}