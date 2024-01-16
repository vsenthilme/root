package com.tekclover.wms.api.enterprise.model.variant;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblvariant")
public class Variant { 
	
	@Id
	@Column(name = "VAR_ID")
	private String variantId;

	@Column(name = "C_ID")
	private String companyId;

	@Column(name = "PLANT_ID")
	private String plantId;
	
	@Column(name = "WH_ID")
	private String warehouseId;
	
	@Column(name = "VAR_SUB_ID")
	private String variantSubId;
	
	@Column(name = "LEVEL_ID")
	private Long levelId;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "variantId",fetch = FetchType.EAGER)
	private Set<LevelReferenceVariant> levelReferenceVariants;

	@Column(name="LEVEL_ID_DESC")
	private String levelIdAndDescription;
	
	@Column(name = "LANG_ID")
	private String languageId;
	@Column(name = "C_TEXT")
	private String description;

	@Column(name="COMP_ID_DESC")
	private String companyIdAndDescription;

	@Column(name="PLANT_ID_DESC")
	private String plantIdAndDescription;

	@Column(name="WH_ID_DESC")
	private String warehouseIdAndDescription;

	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
	private Date createdOn = new Date();

	@Column(name = "UTD_BY")
	private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
