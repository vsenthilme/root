package com.tekclover.wms.api.masters.model.businesspartner;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchBusinessPartner {
	/*
	 * WH_ID
	 * PARTNER_TYP
	 * PARTNER_CODE
	 * PARTNER_NM
	 * STATUS_ID
	 * CTD_BY
	 * CTD_ON
	 * UTD_BY
	 * UTD_ON
	 */
	 
	private List<String> warehouseId;
	private List<String>companyCodeId;
	private List<String>plantId;
	private List<Long> businessPartnerType;
	private List<String> partnerCode;
	private List<String> partnerName;
	private List<Long> statusId;
	private List<String>languageId;
	
	private List<String> createdBy;
	private List<String> updatedBy;
	
	private Date startCreatedOn;
	private Date endCreatedOn;
	
	private Date startUpdatedOn;
	private Date endUpdatedOn;	


}
