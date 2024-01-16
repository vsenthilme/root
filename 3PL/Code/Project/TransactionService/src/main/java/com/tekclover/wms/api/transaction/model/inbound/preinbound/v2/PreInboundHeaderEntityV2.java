package com.tekclover.wms.api.transaction.model.inbound.preinbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundHeaderEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class PreInboundHeaderEntityV2 extends PreInboundHeaderEntity {

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;
}
