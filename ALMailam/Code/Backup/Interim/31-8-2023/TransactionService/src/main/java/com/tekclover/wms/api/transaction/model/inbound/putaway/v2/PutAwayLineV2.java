package com.tekclover.wms.api.transaction.model.inbound.putaway.v2;

import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PutAwayLineV2 extends PutAwayLine {

	@Column(name = "INV_QTY")
	private Double inventoryQuantity;

	@Column(name = "BARCODE_ID", columnDefinition = "nvarchar(255)")
	private String barcodeId;

	@Column(name = "MFR_DATE")
	private Date manufacturerDate;

	@Column(name = "EXP_DATE")
	private Date expiryDate;

	@Column(name = "MFR_CODE", columnDefinition = "nvarchar(255)")
	private String manufacturerCode;

	@Column(name = "MFR_NAME", columnDefinition = "nvarchar(255)")
	private String manufacturerName;

	@Column(name = "ORIGIN", columnDefinition = "nvarchar(255)")
	private String origin;

	@Column(name = "BRAND", columnDefinition = "nvarchar(255)")
	private String brand;

	@Column(name = "ORD_QTY")
	private Double orderQty;

	@Column(name = "CBM", columnDefinition = "nvarchar(255)")
	private String cbm;

	@Column(name = "CBM_UNIT", columnDefinition = "nvarchar(255)")
	private String cbmUnit;

	@Column(name = "CBM_QTY")
	private Double cbmQuantity;

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;
}
