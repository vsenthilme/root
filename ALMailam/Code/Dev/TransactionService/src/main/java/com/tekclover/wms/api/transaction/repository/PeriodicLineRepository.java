package com.tekclover.wms.api.transaction.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.PeriodicLine;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;

@Repository
@Transactional
public interface PeriodicLineRepository extends JpaRepository<PeriodicLine, Long>,
        JpaSpecificationExecutor<PeriodicLine>, StreamableJpaSpecificationRepository<PeriodicLine> {

    public List<PeriodicLine> findAll();

    public PeriodicLine findByWarehouseIdAndCycleCountNoAndStorageBinAndItemCodeAndPackBarcodesAndDeletionIndicator(
            String warehouseId, String cycleCountNo, String storageBin, String itemCode, String packBarcodes, long l);

    public List<PeriodicLine> findByCycleCountNoAndDeletionIndicator(String cycleCountNo, long l);

    //	SELECT ITM_CODE FROM tblperiodicline
//	WHERE WH_ID = 110 AND ITM_CODE IN :itemCode AND STATUS_ID <> 70
    @Query(value = "SELECT ITM_CODE FROM tblperiodicline \r\n"
            + "WHERE WH_ID = 110 AND ITM_CODE IN :itemCode \r\n"
            + "AND STATUS_ID <> 70 AND IS_DELETED = 0", nativeQuery = true)
    public List<String> findByStatusIdNotIn70(@Param(value = "itemCode") Set<String> itemCode);

    List<PeriodicLine> findByCompanyCodeAndLanguageIdAndPlantIdAndWarehouseIdAndStatusIdInAndDeletionIndicator(
            String companyCode, String languageId, String plantId, String warehouseId, List<Long> statusId, Long deletionIndicator);

    List<PeriodicLine> findByCompanyCodeAndLanguageIdAndPlantIdAndWarehouseIdAndCycleCounterIdAndStatusIdInAndDeletionIndicator(
            String companyCode, String languageId, String plantId, String warehouseId,String cycleCounterId, List<Long> statusId, Long deletionIndicator);


    @Query(value = "SELECT COUNT(*) AS count FROM tblperiodicline WHERE "
            + "(:languageId IS NULL OR LANG_ID = :languageId) AND "
            + "(:companyCode IS NULL OR C_ID = :companyCode) AND "
            + "(:plantId IS NULL OR PLANT_ID = :plantId) AND "
            + "(:warehouseId IS NULL OR WH_ID = :warehouseId) AND "
            + "(STATUS_ID IN (:statusId)) AND "
            + "(COUNTER_ID IN (:counterId)) AND "
            + "IS_DELETED = 0", nativeQuery = true)
    public Long getPeriodicLineCount(@Param("companyCode") List<String> companyCode,
                                            @Param("plantId") List<String> plantId,
                                            @Param("warehouseId") List<String> warehouseId,
                                            @Param("languageId") List<String> languageId,
                                            @Param("statusId") List<Long> statusId,
                                            @Param("counterId") List<String> counterId);
}