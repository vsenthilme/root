package com.tekclover.wms.core.batch.dto.v2;

import lombok.Data;

@Data
public class IMPartnerV2 {
	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String itemCode;
	private String businessPartnerCode;
	private String businessPartnerType;
	private String partnerItemBarcode;
	private String manufacturerCode;
	private String manufacturerName;
	private String partnerName;
	private String vendorItemBarcode;
	private String partnerItemNo;
	private String mfrBarcode;
	private String brandName;
	private Double stock;
	private String stockUom;
	private Long statusId;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private Long deletionIndicator;
	private String createdBy;


	/**
	 *
	 * @param languageId
	 * @param companyCodeId
	 * @param plantId
	 * @param warehouseId
	 * @param itemCode
	 * @param businessPartnerCode
	 * @param businessPartnerType
	 * @param partnerItemBarcode
	 * @param manufacturerCode
	 * @param manufacturerName
	 * @param brandName
	 * @param partnerName
	 * @param partnerItemNo
	 * @param vendorItemBarcode
	 * @param mfrBarcode
	 * @param stock
	 * @param stockUom
	 * @param statusId
	 * @param referenceField1
	 * @param referenceField2
	 * @param referenceField3
	 * @param referenceField4
	 * @param referenceField5
	 * @param referenceField6
	 * @param referenceField7
	 * @param referenceField8
	 * @param referenceField9
	 * @param referenceField10
	 * @param deletionIndicator
	 * @param createdBy
	 */
	public IMPartnerV2 (String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
						String businessPartnerCode,String businessPartnerType, String partnerItemBarcode, String manufacturerCode,
						String manufacturerName, String brandName,String partnerName,String partnerItemNo,String vendorItemBarcode,
						String mfrBarcode,Double stock,String stockUom, Long statusId,String referenceField1, String referenceField2,
						String referenceField3,String referenceField4,String referenceField5,String referenceField6,
						String referenceField7,String referenceField8,String referenceField9,String referenceField10,
						Long deletionIndicator, String createdBy) {

		this.languageId = languageId;
		this.companyCodeId = companyCodeId;
		this.plantId = plantId;
		this.warehouseId = warehouseId;
		this.itemCode = itemCode;
		this.businessPartnerType = businessPartnerType;
		this.businessPartnerCode = businessPartnerCode;
		this.partnerItemBarcode = partnerItemBarcode;
		this.manufacturerCode = manufacturerCode;
		this.manufacturerName = manufacturerName;
		this.brandName = brandName;
		this.partnerName = partnerName;
		this.partnerItemNo = partnerItemNo;
		this.vendorItemBarcode = vendorItemBarcode;
		this.mfrBarcode = mfrBarcode;

		if(stock != null) {
			this.stock = stock;
		} else {
			this.stock = 0D;
		}
		this.stockUom = stockUom;

		if(statusId != null) {
			this.statusId = statusId;
		} else {
			this.statusId = 0L;
		}
		this.referenceField1 = referenceField1;
		this.referenceField2 = referenceField2;
		this.referenceField3 = referenceField3;
		this.referenceField4 = referenceField4;
		this.referenceField5 = referenceField5;
		this.referenceField6 = referenceField6;
		this.referenceField7 = referenceField7;
		this.referenceField8 = referenceField8;
		this.referenceField9 = referenceField9;
		this.referenceField10 = referenceField10;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
	}
}
