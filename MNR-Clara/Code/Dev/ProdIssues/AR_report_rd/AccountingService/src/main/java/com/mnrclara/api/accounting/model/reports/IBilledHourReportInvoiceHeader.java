package com.mnrclara.api.accounting.model.reports;

import java.util.Date;

public interface IBilledHourReportInvoiceHeader {

	public String getMatterNumber();
    public String getInvoiceNumber();
    public Double getInvoiceAmount();
    public String getPreBillNumber();
    public Date getDateOfBill();
}
