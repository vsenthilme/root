package com.mnrclara.api.accounting.model.invoice.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblbillingreport")
public class BillingReport {
	@Id
	@Column(name = "BILLING_ID")
	private Long billingId;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "CLIENT_NO")
	private String clientId;
	
	@Column(name = "MATTER_NO")
	private String matterNumber;
	
	@Column(name = "BILLING_DATE")
	private Date billingDate;
	
	@Column(name = "POSTING_DATE")
	private Date postingDate;
	
	@Column(name = "INV_NO")
	private String invoiceNumber;
	
	@Column(name = "FEE_BILLED")
	private Double feeBilled;
	
	@Column(name = "REMAIN_BALANCE")
	private Double remainingBalance;
	
	@Column(name = "TOTAL_BILLED")
	private Double totalBilled;
	
	@Column(name = "MATTER_NAME")
	private String matterName;
	
	@Column(name = "CAT_ID")
	private Long categoryId;
	
	@Column(name = "SUB_CAT_ID")
	private Long subCategoryId;
	
	@Column(name = "BILLING_MODE")
	private String billingMode;
	
	@Column(name = "CLIENT_NAME")
	private String clientName;
	
	@Column(name = "HARD_COST")
	private Double hardCost;
	
	@Column(name = "SOFT_COST")
	private Double softCost;
	
	@Column(name = "ADMIN_COST")
	private Double adminCost;
	
	@Column(name = "TOTAL_HOURS")
	private Double totalHours;
	
	@Column(name = "MISC_DEBITS")
	private Double miscDebits;
	
	@Column(name = "CLASS_ID_DESC")
	private String classIdDescription;
	
	@Column(name = "BILL_MODE_DESC")
	private String billingModeDescription;
	
	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "PARTNER_ASSIGNED")
	private String partnerAssigned;
	
	@Column(name = "RESPONSIBLE_TK")
	private String responsibleTimeKeeper;

	@Column(name = "PAID_AMOUNT")
	private Double paidAmount;

	@Column(name = "ASSIGNED_TK")
	private String assignedTimeKeeper;

	@Column(name = "PARTNER")				//matter assignment partner
	private String partner;
}
