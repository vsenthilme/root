package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
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
