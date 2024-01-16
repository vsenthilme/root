package com.iwmvp.api.master.model.loyaltysetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindLoyaltySetup {
    private List<String> companyId;
    private List<String> categoryId;
    private List<Long> loyaltyId;
    private List<String> status;
    private List<String> createdBy;
    private Date startDate;
    private Date endDate;
}
