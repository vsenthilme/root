package com.tekclover.wms.api.enterprise.model.itemtype;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `ITM_TYPE_ID`
 */
@Table(
		name = "tblitemtype", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_itemtype", 
						columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "ITM_TYPE_ID"})
				}
		)
@IdClass(ItemTypeCompositeKey.class)
public class ItemType { 
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Id
	@Column(name = "C_ID")
	private String companyId;
	
	@Id
	@Column(name = "PLANT_ID") 
	private String plantId;
	
	@Id
	@Column(name = "WH_ID") 
	private String warehouseId;
	
	@Id
	@Column(name = "ITM_TYPE_ID")
	private Long itemTypeId;
	
	@Column(name = "ITM_TYP_TEXT") 
	private String description;
	
	@Column(name = "ST_MTD")
	private String storageMethod;
	
	@Column(name = "VAR_MGT_IND") 
	private Long variantManagementIndicator;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator;
	
	@Column(name = "CTD_BY")
	private String createdBy;
	
	@Column(name = "CTD_ON") 
	private Date createdOn = new Date();
	
	@Column(name = "UTD_BY") 
	private String updatedBy;
	
	@Column(name = "UTD_ON") 
	private Date updatedOn = new Date();
}
