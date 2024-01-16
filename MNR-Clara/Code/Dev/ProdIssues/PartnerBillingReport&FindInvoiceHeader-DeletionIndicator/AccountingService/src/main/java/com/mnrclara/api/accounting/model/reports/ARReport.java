package com.mnrclara.api.accounting.model.reports;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(	name = "tblarreport")
public class ARReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AR_RREPORT_ID") 
	private Long arrReportID;
	
	@Column(name = "CLASS_ID")
    public Long classId;
	
	@Column(name = "CLIENT_ID")
    public String clientId;
	
	@Column(name = "CLIENT_NAME")
    public String clientName;
	
	@Column(name = "MATTER_NO")
    public String matterNumber;
	
	@Column(name = "CASE_CAT_ID")
    public Long caseCategory;
	
	@Column(name = "CASE_SUB_CAT_ID")
    public Long caseSubCategory;
	
	@Column(name = "MATTER_TEXT")
    public String matterText;
	
	@Column(name = "BILLING_FORMAT")
    public String billingFormat;
	
	@Column(name = "PHONE")
    public String phone;
	
	@Column(name = "FEES_DUE")
    public Double feesDue;
	
	@Column(name = "HARD_COST_DUE")
    public Double hardCostsDue;
	
	@Column(name = "SOFT_COST_DUE")
    public Double softCostsDue;
	
	@Column(name = "TOTAL_DUE")
    public Double totalDue;
	
	@Column(name = "POSTING_DATE")
    public Date postingDate;
	
	@Column(name = "LAST_BILL_DATE")
    public Date lastBillDate;
	
	@Column(name = "LAST_PAYMENT_DATE")
    public Date lastPaymentDate;
}
