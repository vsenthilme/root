package com.tekclover.wms.api.transaction.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class StorageBinV2 extends StorageBin {

	@Column(name = "CAP_CHECK")
	private boolean capacityCheck;

	@Column(name="ALLOC_VOL")
	private Double allocatedVolume;

}
