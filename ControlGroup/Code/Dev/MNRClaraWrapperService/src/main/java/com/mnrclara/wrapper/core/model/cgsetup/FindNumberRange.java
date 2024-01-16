package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

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
}
