package com.mnrclara.api.cg.setup.model.numberange;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindNumberRange {

    private List<String> languageId;
    private List<String> companyId;
    private List<Long> numberRangeCode;
    private List<String> numberRangeObject;
    private List<Long> numberRangeStatus;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
