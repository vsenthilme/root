package com.mnrclara.api.accounting.model.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(	name = "tblarreportinv")
public class ARReportInvoice {

	@Id
	@Column(name = "MATTER_NO")
	public String matterNumber;

	@Column(name = "INVOICE_AMOUNT")
	public Double invoiceAmount;

	@Column(name = "FEE_BILLED")
	public Double feeBilled;

	@Column(name = "COST_BILLED")
	public Double costBilled;

	@Column(name = "INVOICE_DATE")
	public Date invoiceDate;
}
