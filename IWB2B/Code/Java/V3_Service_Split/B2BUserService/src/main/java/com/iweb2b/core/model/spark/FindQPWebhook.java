package com.iweb2b.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindQPWebhook{

    private List<String> reference_number;
    private List<String> customer_code;
    private List<String> customer_reference_number;
    private List<String> awb_3rd_Party;
    private List<String> hubCode_3rd_Party;
    private List<String> orderType;
    private Date startDate;
    private Date EndDate;

}
