package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2.PerpetualZeroStockLine;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PerpetualZeroStkLineRepository extends JpaRepository<PerpetualZeroStockLine, Long>,
        JpaSpecificationExecutor<PerpetualZeroStockLine>, StreamableJpaSpecificationRepository<PerpetualZeroStockLine> {


}