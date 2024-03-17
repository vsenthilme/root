package com.mnrclara.api.accounting.model.invoice.report;

import java.util.Date;

public interface IInvoiceActivity {
	
	public String getInvoiceNumber();
	public Date getPostingDate();
	public String getPreBillNumber();
	public Long getItemNumber();
	public Double getBillAmount();
	public String getInvoiceDesc();
	public String getInvoiceRemarks();
}
