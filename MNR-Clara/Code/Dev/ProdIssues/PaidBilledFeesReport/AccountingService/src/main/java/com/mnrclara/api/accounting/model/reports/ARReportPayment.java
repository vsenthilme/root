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
@Table(	name = "tblarreportpu")
public class ARReportPayment {

	@Id
	@Column(name = "MATTER_NO")
    public String matterNumber;

	@Column(name = "PAID_AMOUNT")
    public Double paidAmount;
	
	@Column(name = "PAYMENT_DATE")
    public Date paymentDate;

}
