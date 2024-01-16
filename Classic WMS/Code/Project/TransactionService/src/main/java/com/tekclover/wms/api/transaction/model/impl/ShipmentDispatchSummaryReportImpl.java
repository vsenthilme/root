package com.tekclover.wms.api.transaction.model.impl;

import java.time.Instant;
import java.util.Date;

public interface ShipmentDispatchSummaryReportImpl {
    String getSoNumber();
    String getPartnerCode();
    Date getOrderReceiptTime();
    Double getLinesOrdered();
    Double getLinesShipped();
    Double getOrderedQty();
    Double getShippedQty();
    Double getPercentageShipped();
}
