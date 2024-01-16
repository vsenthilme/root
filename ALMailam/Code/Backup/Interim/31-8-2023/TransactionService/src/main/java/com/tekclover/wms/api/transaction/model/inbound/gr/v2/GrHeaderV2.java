package com.tekclover.wms.api.transaction.model.inbound.gr.v2;

import com.tekclover.wms.api.transaction.model.inbound.gr.GrHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
public class GrHeaderV2 extends GrHeader {



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
