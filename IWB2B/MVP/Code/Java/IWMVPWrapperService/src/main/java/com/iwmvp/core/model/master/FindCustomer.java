package com.iwmvp.core.model.master;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class FindCustomer {
    private List<String> companyId;
    private List<Long> customerId;
    private List<String> customerType;
    private String customerName;
    private List<String> customerCategory;
    private String phoneNo;
    private String alternatePhoneNo;
    private List<String> civilId;
    private String emailId;
    private List<String> city;
    private List<String> country;
    private List<String> userName;
    private List<String> status;
    private List<String> createdBy;
    private Date startDate;
    private Date endDate;
}
