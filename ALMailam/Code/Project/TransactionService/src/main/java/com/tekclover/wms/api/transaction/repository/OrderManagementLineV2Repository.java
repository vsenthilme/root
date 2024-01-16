package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2.OrderManagementLineV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderManagementLineV2Repository extends JpaRepository<OrderManagementLineV2,Long>,
		JpaSpecificationExecutor<OrderManagementLineV2>, StreamableJpaSpecificationRepository<OrderManagementLineV2> {
	

}