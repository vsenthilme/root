package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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

    List<GrHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndCaseCodeAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String goodsReceiptNo, String caseCode, String refDocNumber, Long deletionIndicator);

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
}