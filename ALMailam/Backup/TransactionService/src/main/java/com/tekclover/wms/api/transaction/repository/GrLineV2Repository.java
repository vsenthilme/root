package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrLineV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GrLineV2Repository extends JpaRepository<GrLineV2, Long>, JpaSpecificationExecutor<GrLineV2>,
        StreamableJpaSpecificationRepository<GrLineV2> {


    List<GrLineV2> findByGoodsReceiptNoAndItemCodeAndLineNoAndLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndWarehouseIdAndPreInboundNoAndCaseCodeAndDeletionIndicator(
            String goodsReceiptNo, String itemCode, Long lineNo, String languageId,
            String companyCode, String plantId, String refDocNumber, String packBarcodes,
            String warehouseId, String preInboundNo, String caseCode, Long deletionIndicator);

    Optional<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndPackBarcodesAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String goodsReceiptNo, String palletCode, String caseCode,
            String packBarcodes, Long lineNo, String itemCode, Long deletionIndicator);

    List<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndRefDocNumberAndPackBarcodesAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String preInboundNo, String refDocNumber, String packBarcodes,
            Long lineNo, String itemCode, Long deletionIndicator);

    List<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndWarehouseIdAndPreInboundNoAndCaseCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String refDocNumber, String packBarcodes, String warehouseId,
            String preInboundNo, String caseCode, Long deletionIndicator);

    List<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String refDocNumber, String packBarcodes, Long deletionIndicator);

    List<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            Long lineNo, String itemCode, Long deletionIndicator);

    List<GrLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String refDocNumber, String packBarcodes, Long deletionIndicator);

    List<GrLineV2> findByGoodsReceiptNoAndItemCodeAndLineNoAndLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndWarehouseIdAndPreInboundNoAndCaseCodeAndCreatedOnAndDeletionIndicator(
            String goodsReceiptNo, String itemCode, Long lineNo,
            String languageId, String companyCode, String plantId,
            String refDocNumber, String packBarcodes, String warehouseId,
            String preInboundNo, String caseCode, Date createdOn, Long deletionIndicator);
}