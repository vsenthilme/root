package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.impl.StockMovementReportImpl;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InboundLineV2Repository extends JpaRepository<InboundLineV2, Long>,
        JpaSpecificationExecutor<InboundLineV2>, StreamableJpaSpecificationRepository<InboundLineV2> {

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

    List<InboundLineV2> findByRefDocNumberAndDeletionIndicator(String refDocNumber, Long deletionIndicator);

    List<InboundLineV2> findByRefDocNumberAndCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndDeletionIndicator(
            String refDocNumber, String companyCode, String plantId, String languageId, String warehouseId, Long deletionIndicator);

    List<InboundLineV2> findByCompanyCodeAndLanguageIdAndPlantIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String companyCode, String languageId, String plantId, String warehouseId, String refDocNumber, Long deletionIndicator);

    @Query(value = "select il.wh_id as warehouseId, il.itm_code as itemCode, 'InBound' as documentType ,il.ref_doc_no as documentNumber, il.partner_code as partnerCode, "
            + " il.c_id as companyCodeId,il.plant_id as plantId,il.lang_id as languageId, "
            + " il.c_text as companyDescription,il.plant_text as plantDescription,il.status_text as statusDescription,il.wh_text as warehouseDescription, "
            + " (COALESCE(il.accept_qty,0) + COALESCE(il.damage_qty,0)) as movementQty, il.text as itemText ,il.mfr_name as manufacturerSKU from tblinboundline il "
//            + " join tblimbasicdata1 im on il.itm_code = im.itm_code "
            + "WHERE il.ITM_CODE in (:itemCode) AND "
//            + "im.WH_ID in (:warehouseId) AND "
            + "il.C_ID in (:companyCodeId) AND il.PLANT_ID in (:plantId) AND il.LANG_ID in (:languageId) AND il.WH_ID in (:warehouseId) AND il.status_id in (:statusId) "
            + " AND (il.accept_qty is not null OR il.damage_qty is not null)",
            nativeQuery = true)
    public List<StockMovementReportImpl> findInboundLineForStockMovement(@Param("itemCode") List<String> itemCode,
                                                                         @Param("warehouseId") List<String> warehouseId,
                                                                         @Param("companyCodeId") List<String> companyCodeId,
                                                                         @Param("plantId") List<String> plantId,
                                                                         @Param("languageId") List<String> languageId,
                                                                         @Param("statusId") List<Long> statusId);

    @Query(value = "Select top 1 PA_CNF_ON from tblputawayline where ref_doc_no = :refDocNo and itm_code = :itemCode " +
            "and c_id = :companyCodeId and plant_id = :plantId and lang_id = :languageId and wh_id = :warehouseId " +
            "and mfr_name = :manufacturerName order by PA_CNF_ON DESC",
            nativeQuery = true)
    public Date findDateFromPutawayLine(@Param("refDocNo") String refDocNo, @Param("itemCode") String itemCode,
                                        @Param("manufacturerName") String manufacturerName,
                                        @Param("warehouseId") String warehouseId,
                                        @Param("companyCodeId") String companyCodeId,
                                        @Param("plantId") String plantId,
                                        @Param("languageId") String languageId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE InboundLineV2 ib SET ib.statusId = :statusId, ib.confirmedBy = :confirmedBy, ib.confirmedOn = :confirmedOn, ib.statusDescription = :statusDescription \n" +
            "WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber and ib.companyCode = :companyCode and ib.plantId = :plantId and ib.languageId = :languageId and ib.statusId = 20")
    void updateInboundLineStatus(@Param("warehouseId") String warehouseId,
                                 @Param("companyCode") String companyCode,
                                 @Param("plantId") String plantId,
                                 @Param("languageId") String languageId,
                                 @Param("refDocNumber") String refDocNumber,
                                 @Param("statusId") Long statusId,
                                 @Param("statusDescription") String statusDescription,
                                 @Param("confirmedBy") String confirmedBy,
                                 @Param("confirmedOn") Date confirmedOn);

    List<InboundLineV2> findByRefDocNumberAndCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndDeletionIndicator(
            String refDocNumber, String companyCode, String plantId, String languageId, String warehouseId, Long statusId, Long deletionIndicator);
}

