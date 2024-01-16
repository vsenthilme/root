package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundHeaderEntityV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PreInboundHeaderV2Repository extends JpaRepository<PreInboundHeaderEntityV2, Long>,
        JpaSpecificationExecutor<PreInboundHeaderEntityV2>, StreamableJpaSpecificationRepository<PreInboundHeaderEntityV2> {

    public List<PreInboundHeaderEntityV2> findAll();

    public Optional<PreInboundHeaderEntityV2> findByPreInboundNoAndWarehouseIdAndDeletionIndicator(
            String preInboundNo, String warehouseId, Long deletionIndicator);

    public PreInboundHeaderEntityV2 findByWarehouseId(String warehouseId);

    public Optional<PreInboundHeaderEntityV2>
    findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, Long deletionIndicator);

    // Pass WH_ID in PREINBOUNDHEADER table and fetch the Count of values where STATUS_ID=06,07 and Autopopulate
    public long countByWarehouseIdAndStatusIdIn(String warehouseId, List<Long> statusId);


    public List<PreInboundHeaderEntityV2> findByWarehouseIdAndStatusIdAndDeletionIndicator(
            String warehouseId, Long statusId, Long deletionIndicator);

    public Optional<PreInboundHeaderEntityV2> findByWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String warehouseId, String preInboundNo, String refDocNumner, Long deletionIndicator);

    public Optional<PreInboundHeaderEntityV2> findByPreInboundNoAndDeletionIndicator(String preInboundNo, Long deletionIndicator);

    public Optional<PreInboundHeaderEntityV2> findByRefDocNumberAndDeletionIndicator(String refDocNumber, Long deletionIndicator);

    @Query(value = "Select REF_DOC_TYP from tblpreinboundheader where WH_ID = :warehouseId and ref_doc_no = :refDocNumber \n" +
            " and PRE_IB_NO = :preInboundNo and IS_DELETED = :delete ", nativeQuery = true)
    public String getReferenceDocumentTypeFromPreInboundHeader(@Param("warehouseId") String warehouseId,
                                                               @Param("preInboundNo") String preInboundNo,
                                                               @Param("refDocNumber") String refDocNumber,
                                                               @Param("delete") Long delete);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PreInboundHeaderEntityV2 ib SET ib.statusId = :statusId WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber")
    void updatePreInboundHeaderEntityV2Status(@Param("warehouseId") String warehouseId,
                                              @Param("refDocNumber") String refDocNumber,
                                              @Param("statusId") Long statusId);

    Optional<PreInboundHeaderEntityV2> findByPreInboundNoAndWarehouseIdAndCompanyCodeAndPlantIdAndLanguageIdAndDeletionIndicator(
            String preInboundNo, String warehouseId, String companyCode,
            String plantId, String languageId, Long deletionIndicator);

    List<PreInboundHeaderEntityV2> findByWarehouseIdAndCompanyCodeAndPlantIdAndLanguageIdAndStatusIdAndDeletionIndicator(
            String warehouseId, String companyCode, String plantId, String languageId,
            Long statusId, long deletionIndicator);

    Optional<PreInboundHeaderEntityV2> findByCompanyCodeAndPlantIdAndLanguageIdAndPreInboundNoAndWarehouseIdAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String preInboundNo, String warehouseId, Long deletionIndicator);

    Optional<PreInboundHeaderEntityV2> findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String preInboundNo, String refDocNumner, Long deletionIndicator);
}