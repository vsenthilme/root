package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.List;

@Data
public class SearchGrLine {


    private List<String> preInboundNo;
    private List<String> refDocNumber;
    private List<String> packBarcodes;
    private List<Long> lineNo;
    private List<String> itemCode;
    private List<String> caseCode;
    private List<Long> statusId;

}
