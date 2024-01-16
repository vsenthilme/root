package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundHeaderV2;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundHeaderV2Repository extends JpaRepository<OutboundHeaderV2,Long>, JpaSpecificationExecutor<OutboundHeaderV2> {

}