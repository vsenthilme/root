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

 	@Column(name = "MONTH_APPROVED_AMOUNT")
	public Double mApprovedAmount;

	@Column(name = "YEAR_APPROVED_AMOUNT")
	public Double yApprovedAmount;
}