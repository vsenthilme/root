package com.iwmvp.core.model.master;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindNumberRange {
    private List<String> companyId;
    private List<Long> numberRangeCode;
    private List<String> numberRangeObject;
    private List<Long> numberRangeCurrent;
    private List<Long> numberRangeStatus;
    private List<String> createdBy;
    private Date startDate;
    private Date endDate;
}
