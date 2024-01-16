package com.tekclover.wms.api.transaction.model.inbound.gr.v2;

import com.tekclover.wms.api.transaction.model.inbound.gr.GrHeader;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class GrHeaderV2 extends GrHeader {


	@Column(name = "BILL_STATUS", columnDefinition = "nvarchar(100)")
	private String billStatus;

	@Column(name = "LENGTH")
	private Double length;

	@Column(name = "WIDTH")
	private Double width;

	@Column(name = "HEIGHT")
	private Double height;

	@Column(name = "CBM")
	private Double cbm;

	@Column(name = "CBM_UNIT", columnDefinition = "nvarchar(255)")
	private String cbmUnit;

	@Column(name = "ACCEPT_QTY")
	private Double acceptedQuantity;

	@Column(name = "DAMAGE_QTY")
	private Double damagedQuantity;

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;
}
