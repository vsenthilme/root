package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboundLineV2Repository extends JpaRepository<InboundLineV2, Long>,
        JpaSpecificationExecutor<InboundLineV2> {

    @Query(value = "select \n" +
            "* \n" +
            "from \n" +
            "tblinboundline \n" +
            "where \n" +
            "c_id IN (:companyCode) and \n" +
            "lang_id IN (:languageId) and \n" +
            "plant_id IN(:plantId) and \n" +
            "wh_id IN (:warehouseId) and \n" +
            "ib_line_no IN (:lineNo) and \n" +
            "itm_code IN (:itemCode) and \n" +
            "pre_ib_no IN (:preInboundNo) and \n" +
            "ref_doc_no IN (:refDocNumber) and \n" +
            "is_deleted = 0", nativeQuery = true)
    InboundLineV2 getInboundLineV2(
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "lineNo") Long lineNo,
            @Param(value = "preInboundNo") String preInboundNo,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "companyCode") String companyCode,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "refDocNumber") String refDocNumber
    );

    InboundLineV2 findByWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
            String warehouseId, String refDocNumber, String preInboundNo, Long lineNo, String itemCode, Long deletionIndicator);

    Optional<InboundLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber, String preInboundNo, Long lineNo, String itemCode, Long deletionIndicator);

    @Query(value="select (COALESCE(il.accept_qty,0) + COALESCE(il.damage_qty,0)) as quantity \n" +
            "from tblinboundline il where il.itm_code = :itemCode and il.IB_LINE_NO = :lineNo and il.ref_doc_no = :refDocNo and \n" +
            "il.PRE_IB_NO = :preInboundNo and il.wh_id = :warehouseId and il.IS_DELETED = 0 " ,nativeQuery=true)
    public Double getQuantityByRefDocNoAndPreInboundNoAndLineNoAndItemCode(@Param("itemCode") String itemCode,
                                                                           @Param ("refDocNo") String refDocNo,
                                                                           @Param ("preInboundNo") String preInboundNo,
                                                                           @Param ("lineNo") Long lineNo,
                                                                           @Param ("warehouseId") String warehouseId);

    List<InboundLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber, String preInboundNo, Long deletionIndicator);

    List<InboundLineV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndReferenceField1AndStatusIdAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String refDocNumber,
            String preInboundNo, String referenceField1, Long statusId, Long deletionIndicator);

}

