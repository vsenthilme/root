package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayHeader;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
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
public interface PutAwayHeaderV2Repository extends JpaRepository<PutAwayHeaderV2, Long>,
        JpaSpecificationExecutor<PutAwayHeaderV2>, StreamableJpaSpecificationRepository<PutAwayHeaderV2> {


    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndPutAwayNumberAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String putAwayNumber, Long deletionIndicator);

    Optional<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndPackBarcodesAndPutAwayNumberAndProposedStorageBinAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String goodsReceiptNo, String palletCode, String caseCode,
            String packBarcodes, String putAwayNumber, String proposedStorageBin, Long deletionIndicator);

    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber, String packBarcodes, Long deletionIndicator);

    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber, Long deletionIndicator);

    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndRefDocNumberAndStatusIdInAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String refDocNumber, List<Long> statusIds, Long deletionIndicator);

    List<PutAwayHeaderV2> findByWarehouseIdAndStatusIdAndInboundOrderTypeIdInAndDeletionIndicator(
            String warehouseId, Long statusId, List<Long> orderTypeId, Long deletionIndicator);

    @Query(value = "SELECT * from tblputawayheader \n"
            + "where pa_no = :putAwayNumber and is_deleted = 0", nativeQuery = true)
    public PutAwayHeaderV2 getPutAwayHeaderV2(@Param(value = "putAwayNumber") String putAwayNumber);

    PutAwayHeaderV2 findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndPutAwayNumberAndDeletionIndicator(
            String companyId, String plantId, String warehouseId, String languageId, String putAwayNumber, Long deletionIndicator);

    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber, String preInboundNumber, Long deletionIndicator);

    PutAwayHeaderV2 findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndPreInboundNoAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
            String companyCodeId, String plantId, String warehouseId, String languageId,
            String preInboundNo, String refDocNumber, String packBarcodes, Long deletionIndicator);

    List<PutAwayHeaderV2> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndReferenceField5AndManufacturerNameAndStatusIdAndDeletionIndicator(
            String companyCodeId, String plantId, String warehouseId, String languageId,
            String itemCode, String manufacturerName, Long statusId, Long deletionIndicator);

    List<PutAwayHeaderV2> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndStatusIdAndDeletionIndicator(
            String companyCodeId, String plantId, String warehouseId, String languageId, Long statusId, Long deletionIndicator);

    List<PutAwayHeaderV2> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndReferenceField5AndManufacturerNameAndStatusIdAndDeletionIndicatorOrderByCreatedOn(
            String companyCodeId, String plantId, String warehouseId, String languageId, String itemCode, String manufacturerName, Long statusId, Long deletionIndicator);

    List<PutAwayHeaderV2> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndStatusIdAndDeletionIndicatorOrderByCreatedOn(
            String companyCodeId, String plantId, String warehouseId, String languageId, Long statusId, Long deletionIndicator);

    PutAwayHeaderV2 findByPutAwayNumberAndDeletionIndicator(String putAwayNumber, Long deletionIndicator);

    List<PutAwayHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndStatusIdInAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String refDocNumber, List<Long> statusIds, Long deletionIndicator);

    List<PutAwayHeaderV2> findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String refDocNumber, Long deletionIndicator);

    @Query(value = "SELECT COUNT(*) FROM tblputawayheader WHERE LANG_ID ='EN' AND C_ID = :companyId AND PLANT_ID = :plantId AND WH_ID = :warehouseId \r\n"
            + "AND PRE_IB_NO = :preInboundNo AND REF_DOC_NO = :refDocNumber AND STATUS_ID IN (19) AND IS_DELETED = 0", nativeQuery = true)
    public long getPutawayHeaderCountByStatusId(
            @Param("companyId") String companyId,
            @Param("plantId") String plantId,
            @Param("warehouseId") String warehouseId,
            @Param("preInboundNo") String preInboundNo,
            @Param("refDocNumber") String refDocNumber);
}