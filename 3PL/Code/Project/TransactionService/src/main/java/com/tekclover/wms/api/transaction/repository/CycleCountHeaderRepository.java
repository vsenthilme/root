package com.tekclover.wms.api.transaction.repository;


import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleCountHeaderRepository extends JpaRepository<CycleCountHeader,String> {
}
