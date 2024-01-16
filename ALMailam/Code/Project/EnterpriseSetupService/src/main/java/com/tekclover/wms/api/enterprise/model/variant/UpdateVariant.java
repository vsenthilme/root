package com.tekclover.wms.api.enterprise.model.variant;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateVariant {

	private String variantId;
    private String companyId;
	private String plantId;
    private String warehouseId;
	private String variantSubId;
	private Long levelId;
	private String description;
	private String languageId;
	private Set<LevelReferenceVariant> levelReferenceVariants;
	private String updatedBy;
}
