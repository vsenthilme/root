package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupHeaderV2Repository
		extends JpaRepository<PickupHeaderV2, Long>,
				JpaSpecificationExecutor<PickupHeaderV2>,
				StreamableJpaSpecificationRepository<PickupHeaderV2> {

}