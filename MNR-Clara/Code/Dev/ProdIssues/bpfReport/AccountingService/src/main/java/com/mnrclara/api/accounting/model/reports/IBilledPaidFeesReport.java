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
		name = "tblbpfreporttemp",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "unique_key_tblbpfreporttemp",
						columnNames = {"CASE_CATEGORY_ID" , "CASE_SUB_CATEGORY_ID"})
		}
)
@IdClass(IBilledPaidFeesReportCompositeKey.class)
public class IBilledPaidFeesReport {

	@Id
	@Column(name = "CASE_CATEGORY_ID")
	public Long caseCategoryId;

	@Id
	@Column(name = "CASE_SUB_CATEGORY_ID")
	public Long caseSubCategoryId;

	@Column(name = "AMOUNT")
	public Double amount;

}