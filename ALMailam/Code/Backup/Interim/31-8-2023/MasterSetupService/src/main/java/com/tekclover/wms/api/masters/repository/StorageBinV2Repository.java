package com.tekclover.wms.api.masters.repository;

import com.tekclover.wms.api.masters.model.storagebin.v2.StorageBinV2;
import com.tekclover.wms.api.masters.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StorageBinV2Repository extends JpaRepository<StorageBinV2, Long>,
        JpaSpecificationExecutor<StorageBinV2>,
        StreamableJpaSpecificationRepository<StorageBinV2> {

    List<StorageBinV2> findByWarehouseIdAndStorageBinInAndBinClassIdAndPutawayBlockAndPickingBlockAndDeletionIndicatorOrderByStorageBinDesc(
            String warehouseId, List<String> storageBin, Long binClassId, int i, int j, long l);

    List<StorageBinV2> findByWarehouseIdAndStatusIdAndDeletionIndicator(String warehouseId, Long statusId, long l);

    Optional<StorageBinV2> findByStorageBinAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(
            String storageBin, String companyCodeId, String plantId, String warehouseId, String languageId, long l);

    StorageBinV2 findByWarehouseIdAndStorageBinAndDeletionIndicator(String warehouseId, String storageBin, long l);

    @Query(value = "SELECT * FROM tblstoragebin WHERE st_bin = :storageBin and c_id = :companyCode and plant_id = :plantId and \n" +
            "wh_id = :warehouseId and lang_id = :languageId and is_deleted = 0 ", nativeQuery = true)
    public StorageBinV2 getStorageBin(@Param("storageBin") String storageBin,
                                      @Param("companyCode") String companyCode,
                                      @Param("plantId") String plantId,
                                      @Param("languageId") String languageId,
                                      @Param("warehouseId") String warehouseId);
}


