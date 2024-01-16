package com.iwmvp.core.model.master;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindLoyaltyCategory {
    private List<String> companyId;
    private List<Long> rangeId;
    private List<String> categoryId;
    private List<String> category;
    private List<String> status;
    private List<String> createdBy;
    private Date startDate;
    private Date endDate;
}
