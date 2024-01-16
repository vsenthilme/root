package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

@Data
public class MatterPLReport {

    String clientId;
    String clientName;
    String matterNumber;
    String matterDescription;
    String partnerAssigned;
    String invoiceDate;
    String invoiceNumber;
    Double timeticketCaptured;
    Double costCaptured;
    Double flatFeeBilled;
    Double costBilled;
    Double totalBilled;
    Double matterPandL;
}
