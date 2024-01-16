package com.tekclover.wms.api.transaction.repository;


import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CycleCountLineRepository extends JpaRepository<CycleCountLine,String> {
}
