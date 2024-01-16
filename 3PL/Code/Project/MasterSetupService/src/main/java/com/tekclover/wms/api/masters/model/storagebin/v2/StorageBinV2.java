package com.tekclover.wms.api.masters.model.storagebin.v2;

import com.tekclover.wms.api.masters.model.storagebin.StorageBin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class StorageBinV2 extends StorageBin {

	@Column(name = "CAP_CHECK")
	private Long capacityCheck;

	@Column(name="ALLOC_VOL")
	private Double allocatedVolume;

}
