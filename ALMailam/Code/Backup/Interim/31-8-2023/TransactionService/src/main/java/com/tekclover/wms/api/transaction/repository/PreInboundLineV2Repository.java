package com.tekclover.wms.api.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineEntityV2;

@Repository
public interface PreInboundLineV2Repository extends JpaRepository<PreInboundLineEntityV2, Long>, JpaSpecificationExecutor<PreInboundLineEntityV2> {

    public List<PreInboundLineEntityV2> findByPreInboundNoAndDeletionIndicator(
            String preInboundNo, Long deletionIndicator);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PreInboundLineEntityV2 ib SET ib.statusId = :statusId WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber")
    void updatePreInboundLineStatus(@Param("warehouseId") String warehouseId,
                                    @Param("refDocNumber") String refDocNumber, @Param("statusId") Long statusId);

    public List<PreInboundLineEntityV2> findByWarehouseIdAndPreInboundNoAndDeletionIndicator(String warehouseId,
                                                                                             String preInboundNo, Long deletionIndicator);

    Optional<PreInboundLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, Long lineNo, String itemCode, Long deletionIndicator);

    List<PreInboundLineEntityV2> findByWarehouseIdAndCompanyCodeAndPlantIdAndLanguageIdAndPreInboundNoAndDeletionIndicator(
            String warehouseId, String companyCode, String plantId, String languageId,
            String preInboundNo, Long deletionIndicator);
}