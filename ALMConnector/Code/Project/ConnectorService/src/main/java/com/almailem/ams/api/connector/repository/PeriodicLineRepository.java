package com.almailem.ams.api.connector.repository;

import com.almailem.ams.api.connector.model.periodic.PeriodicLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PeriodicLineRepository extends JpaRepository<PeriodicLine, String>,
        JpaSpecificationExecutor<PeriodicLine> {


    PeriodicLine findByCycleCountNoAndItemCodeAndManufacturerCode(String cycleCountNo, String itemCode, String manufacturerName);
    PeriodicLine findByCycleCountNoAndItemCodeAndManufacturerCodeAndLineNoOfEachItemCode(String cycleCountNo, String itemCode, String manufacturerName, Long lineNo);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE PERIODICLINE set countedQty = :countedQty, isCompleted = :isCompleted  \r\n"
            + " WHERE cycleCountNo = :cycleCountNo and itemCode = :itemCode and \n"
            + "manufacturerName = :manufacturerName and lineNoOfEachItemCode = :lineNo ", nativeQuery = true)
    public void updatePdlLine (
            @Param(value = "countedQty") Double countedQty,
            @Param(value = "isCompleted") Long isCompleted,
            @Param(value = "cycleCountNo") String cycleCountNo,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "manufacturerName") String manufacturerName,
            @Param(value = "lineNo") Long lineNo);
}
