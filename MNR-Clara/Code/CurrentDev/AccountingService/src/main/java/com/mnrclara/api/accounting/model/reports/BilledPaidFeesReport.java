package com.mnrclara.api.accounting.model.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name = "tblbpfreport",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "unique_key_tblbpfreport",
						columnNames = {"TK_CODE" , "CASE_CATEGORY_ID" , "CASE_SUB_CATEGORY_ID"})
		}
)
@IdClass(BilledPaidFeesReportCompositeKey.class)
public class BilledPaidFeesReport {

	@Id
	@Column(name = "TK_CODE")
	public String timekeeperCode;

	@Column(name = "TK_NAME")
	public String timekeeperName;
	@Id
	@Column(name = "CASE_CATEGORY_ID")
	public Long caseCategoryId;
	@Id
	@Column(name = "CASE_SUB_CATEGORY_ID")
	public Long caseSubCategoryId;

	@Column(name = "MONTH_BILLED_AMOUNT")
	public Double mBilledAmount;

	@Column(name = "YEAR_BILLED_AMOUNT")
	public Double yBilledAmount;

 	@Column(name = "MONTH_TIMETICKET_AMOUNT")
	public Double mTimeticketAmount;

	@Column(name = "YEAR_TIMETICKET_AMOUNT")
	public Double yTimeticketAmount;

	@Column(name = "MONTH_INVOICE_AMOUNT")
	public Double mInvoiceAmount;

	@Column(name = "YEAR_INVOICE_AMOUNT")
	public Double yInvoiceAmount;

 	@Column(name = "MONTH_PAYMENT_AMOUNT")
	public Double mPaymentAmount;

	@Column(name = "YEAR_PAYMENT_AMOUNT")
	public Double yPaymentAmount;
}