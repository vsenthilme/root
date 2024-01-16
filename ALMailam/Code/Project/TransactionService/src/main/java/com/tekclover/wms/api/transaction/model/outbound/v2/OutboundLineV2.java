package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.OutboundLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class OutboundLineV2 extends OutboundLine {

	@Column(name = "PICK_LIST_NUMBER", columnDefinition = "nvarchar(255)")
	private String pickListNumber;

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

}
