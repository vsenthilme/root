package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.InboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InboundHeaderV2Repository extends JpaRepository<InboundHeader, Long>,
        JpaSpecificationExecutor<InboundHeader> {

    Optional<InboundHeaderV2> findByWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);
}