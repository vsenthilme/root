package com.mnrclara.api.accounting.model.reports;

public interface IBilledHourReportMatterTimeTicket {

	public Double getHoursBilled();
    public Double getAmountBilled();
    public String getPreBillNumber();
    public String getAttorney();
}
