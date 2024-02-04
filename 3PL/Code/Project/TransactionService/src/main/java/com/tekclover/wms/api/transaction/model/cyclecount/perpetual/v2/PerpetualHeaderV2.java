package com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2;

import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.PerpetualHeader;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(callSuper = true)
public class PerpetualHeaderV2 extends PerpetualHeader {

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;

//	@Column(name = "MIDDLEWARE_ID", columnDefinition = "nvarchar(50)")
//	private String middlewareId;
//
//	@Column(name = "MIDDLEWARE_TABLE", columnDefinition = "nvarchar(50)")
//	private String middlewareTable;
//
//	@Column(name = "REF_DOC_TYPE", columnDefinition = "nvarchar(150)")
//	private String referenceDocumentType;

	@Column(name = "REF_CC_NO", columnDefinition = "nvarchar(150)")
	private String referenceCycleCountNo;

}