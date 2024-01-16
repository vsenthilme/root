package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupLineV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupLineV2Repository extends JpaRepository<PickupLineV2,Long>, JpaSpecificationExecutor<PickupLineV2> {
	

}