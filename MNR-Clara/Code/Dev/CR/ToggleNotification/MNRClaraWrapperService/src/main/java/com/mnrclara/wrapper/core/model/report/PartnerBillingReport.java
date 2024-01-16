package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

@Data
public class PartnerBillingReport {
    private String classId;
    private String classDescription;
    private String clientId;
    private String clientName;
    private String matterNumber;
    private String matterDescription;
    private String partner;
    private String responsibleTimekeeper;
    private String assignedTimekeeper;

    private Double paidAmount;
    private Double totalBilled;
    private Double softCost;
    private Double hardCost;
    private Double totalCost;
    private Double feeBilled;
    private Double balance;
}
