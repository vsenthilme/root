package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboundOrderV2Repository extends JpaRepository<InboundOrderV2, Long> {
    public InboundOrderV2 findByRefDocumentNo(String orderId);

    List<InboundOrderV2> findTopByProcessedStatusIdOrderByOrderReceivedOn(long l);
}