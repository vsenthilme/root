package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GrHeaderV2Repository extends JpaRepository<GrHeaderV2, Long>, JpaSpecificationExecutor<GrHeaderV2>,
        StreamableJpaSpecificationRepository<GrHeaderV2> {


    Optional<GrHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String stagingNo, String goodsReceiptNo, String palletCode, String caseCode, Long deletionIndicator);

    GrHeaderV2 findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndCaseCodeAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String goodsReceiptNo, String caseCode, String refDocNumber, Long deletionIndicator);

    @Query(value = "select \n" +
            "* \n" +
            "from \n" +
            "tblgrheader \n" +
            "where \n" +
            "c_id IN (:companyCode) and \n" +
            "lang_id IN (:languageId) and \n" +
            "plant_id IN(:plantId) and \n" +
            "wh_id IN (:warehouseId) and \n" +
            "gr_no IN (:goodsReceiptNo) and \n" +
            "case_code IN (:caseCode) and \n" +
            "ref_doc_no IN (:refDocNumber) and \n" +
            "is_deleted = 0", nativeQuery = true)
    List<GrHeaderV2> getGrHeaderV2(
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "goodsReceiptNo") String goodsReceiptNo,
            @Param(value = "caseCode") String caseCode,
            @Param(value = "companyCode") String companyCode,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "refDocNumber") String refDocNumber
    );

    List<GrHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String preInboundNo, String refDocNumber, Long deletionIndicator);

    List<GrHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndWarehouseIdAndPreInboundNoAndCaseCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String refDocNumber, String warehouseId, String preInboundNo, String caseCode, Long deletionIndicator);

    GrHeaderV2 findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String refDocNumber, Long deletionIndicator);

    GrHeaderV2 findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String refDocNumber, Long deletionIndicator);

    GrHeaderV2 findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE GrHeaderV2 ib SET ib.statusId = :statusId, ib.statusDescription = :statusDescription \n" +
            "WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber and ib.companyCode = :companyCode and ib.plantId = :plantId and ib.languageId = :languageId")
    void updateGrHeaderStatus(@Param("warehouseId") String warehouseId,
                              @Param("companyCode") String companyCode,
                              @Param("plantId") String plantId,
                              @Param("languageId") String languageId,
                              @Param("refDocNumber") String refDocNumber,
                              @Param("statusId") Long statusId,
                              @Param("statusDescription") String statusDescription);

    @Transactional
    @Procedure(procedureName = "grheader_status_update_proc")
    public void updateGrheaderStatusUpdateProc(
            @Param("companyCodeId") String companyCodeId,
            @Param("plantId") String plantId,
            @Param("languageId") String languageId,
            @Param("warehouseId") String warehouseId,
            @Param("refDocNumber") String refDocNumber,
            @Param("preInboundNo") String preInboundNo,
            @Param("goodsReceiptNo") String goodsReceiptNo,
            @Param("statusId") Long statusId,
            @Param("statusDescription") String statusDescription,
            @Param("updatedOn") Date updatedOn
    );
    @Transactional
    @Procedure(procedureName = "grheader_status_update_ib_cnf_proc")
    public void updateGrheaderStatusUpdateInboundConfirmProc(
            @Param("companyCodeId") String companyCode,
            @Param("plantId") String plantId,
            @Param("languageId") String languageId,
            @Param("warehouseId") String warehouseId,
            @Param("refDocNumber") String refDocNumber,
            @Param("preInboundNo") String preInboundNo,
            @Param("statusId") Long statusId,
            @Param("statusDescription") String statusDescription,
            @Param("updatedBy") String updatedBy,
            @Param("updatedOn") Date updatedOn
    );

    GrHeaderV2 findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);
}