package com.tekclover.wms.core.model.masters;

import lombok.Data;

@Data
public class StorageBinV2 extends StorageBin {

	private Long capacityCheck;

	private Double allocatedVolume;

}
