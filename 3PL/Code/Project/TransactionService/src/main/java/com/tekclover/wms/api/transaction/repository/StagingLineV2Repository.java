package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StagingLineV2Repository extends JpaRepository<StagingLineEntityV2, Long>,
        JpaSpecificationExecutor<StagingLineEntityV2> {

    Optional<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndPalletCodeAndCaseCodeAndLineNoAndItemCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String stagingNo, String palletCode, String caseCode,
            Long lineNo, String itemCode, String manufacturerCode, Long deletionIndicator);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String refDocNumber, String preInboundNo,
            Long lineNo, String itemCode, String manufacturerCode, Long deletionIndicator);

    //    long getStagingLineCountByStatusId(String companyCode, String plantId, String warehouseId,
//                                       String preInboundNo, String refDocNumber, String manufacturerCode);
    @Query(value = "SELECT COUNT(*) FROM tblstagingline WHERE LANG_ID ='EN' \n" +
            "AND C_ID = :companyId AND PLANT_ID = :plantId AND WH_ID = :warehouseId \r\n" +
            "AND PRE_IB_NO = :preInboundNo AND REF_DOC_NO = :refDocNumber AND MFR_CODE = :manufacturerCode AND\n" +
            "STATUS_ID IN (14, 17) AND IS_DELETED = 0", nativeQuery = true)
    public long getStagingLineCountByStatusId(
            @Param("companyId") String companyId,
            @Param("plantId") String plantId,
            @Param("warehouseId") String warehouseId,
            @Param("manufacturerCode") String manufacturerCode,
            @Param("preInboundNo") String preInboundNo,
            @Param("refDocNumber") String refDocNumber);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndCaseCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String refDocNumber, String preInboundNo,
            Long lineNo, String itemCode, String caseCode, String manufacturerCode, Long deletionIndicator);

    @Query(value = "select (COALESCE(i.inv_qty,0) + COALESCE(i.alloc_qty,0)) from tblinventory i \n" +
            "WHERE i.ITM_CODE in (:itemCode) AND i.WH_ID in (:warehouseId) AND \n" +
            "i.LANG_ID in (:languageId) AND i.plant_id in (:plantId) AND i.c_id in (:companyCodeId)", nativeQuery = true)
    public Double getTotalQuantity(
            @Param(value = "itemCode") String itemCode,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "languageId") String languageId,
            @Param(value = "plantId") String plantId,
            @Param(value = "companyCodeId") String companyCodeId);

    //Almailem
    @Query(value = "select sum(totalQuantity) from (select (COALESCE(i.inv_qty,0) + COALESCE(i.alloc_qty,0)) totalQuantity from tblinventory i \n" +
            "WHERE i.ITM_CODE in (:itemCode) AND i.WH_ID in (:warehouseId) AND \n" +
            "i.LANG_ID in (:languageId) AND i.plant_id in (:plantId) AND i.c_id in (:companyCodeId)) x", nativeQuery = true)
    public Double getTotalQuantityNew(
            @Param(value = "itemCode") String itemCode,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "languageId") String languageId,
            @Param(value = "plantId") String plantId,
            @Param(value = "companyCodeId") String companyCodeId);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndLineNoAndItemCodeAndCaseCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String warehouseId, String preInboundNo, String refDocNumber,
            String stagingNo, Long lineNo, String itemCode, String caseCode,
            String manufacturerCode, Long deletionIndicator);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndLineNoAndItemCodeAndStatusIdInAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId,
            String preInboundNo, Long lineNo, String itemCode,
            List<Long> statusIds, String manufacturerCode, Long deletionIndicator);

    //Company Description
    @Query(value = "select c_text from tblcompanyid tc \n" +
            "WHERE tc.c_id in (:companyCodeId) and \n" +
            "tc.is_deleted = 0", nativeQuery = true)
    public String getCompanyDescription(@Param(value = "companyCodeId") String companyCodeId);

    //Plant Description
    @Query(value = "select plant_text from tblplantid tp \n" +
            "WHERE tp.c_id in (:companyCodeId) and tp.plant_id in (:plantId) and \n" +
            "tp.is_deleted = 0", nativeQuery = true)
    public String getPlantDescription(@Param(value = "companyCodeId") String companyCodeId,
                                      @Param(value = "plantId") String plantId);

    //Warehouse Description
    @Query(value = "select wh_text from tblwarehouseid wh \n" +
            "WHERE wh.c_id in (:companyCodeId) and wh.plant_id in (:plantId) and wh.wh_id in (:warehouseId) and \n" +
            "wh.is_deleted = 0", nativeQuery = true)
    public String getWarehouseDescription(@Param(value = "companyCodeId") String companyCodeId,
                                          @Param(value = "plantId") String plantId,
                                          @Param(value = "warehouseId") String warehouseId);

    //Partner_item_barcode
    @Query(value = "select string_agg(partner_itm_bar,', ') from tblimpartner ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.is_deleted = 0", nativeQuery = true)
    public String getPartnerItemBarcode(@Param(value = "itemCode") String itemCode);

    //Partner_item_barcode - almailem
    @Query(value = "select string_agg(partner_itm_bar,', ') from (select distinct partner_itm_bar from tblimpartner ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.c_id in (:companyCode) and \n" +
            "ip.plant_id in (:plantId) and \n" +
            "ip.wh_id in (:warehouseId) and \n" +
            "ip.lang_id in (:languageId) and \n" +
            "ip.mfr_code in (:manufactureCode) and \n" +
            "ip.is_deleted = 0) x", nativeQuery = true)
    public String getPartnerItemBarcode(@Param(value = "itemCode") String itemCode,
                                        @Param(value = "companyCode") String companyCode,
                                        @Param(value = "plantId") String plantId,
                                        @Param(value = "warehouseId") String warehouseId,
                                        @Param(value = "manufactureCode") String manufactureCode,
                                        @Param(value = "languageId") String languageId);

    //Partner_item_barcode - almailem - interim only
    @Query(value = "select string_agg(barcode,', ') from (select distinct barcode from tblinterimbarcodeid ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.ref_field_1 in (:manufactureCode) ) x", nativeQuery = true)
    public String getPartnerItemBarcode(@Param(value = "itemCode") String itemCode,
                                        @Param(value = "manufactureCode") String manufactureCode);


    //Description
    @Query(value = "select tc.c_text companyDesc,\n" +
            "tp.plant_text plantDesc,\n" +
            "tw.wh_text warehouseDesc from \n" +
            "tblcompanyid tc\n" +
            "join tblplantid tp on tp.c_id = tc.c_id and tp.lang_id = tc.lang_id\n" +
            "join tblwarehouseid tw on tw.c_id = tc.c_id and tw.lang_id=tc.lang_id and tw.plant_id = tp.plant_id\n" +
            "where\n" +
            "tc.c_id IN (:companyCodeId) and \n" +
            "tc.lang_id IN (:languageId) and \n" +
            "tp.plant_id IN(:plantId) and \n" +
            "tw.wh_id IN (:warehouseId) and \n" +
            "tc.is_deleted=0 and \n" +
            "tp.is_deleted=0 and \n" +
            "tw.is_deleted=0", nativeQuery = true)
    public IKeyValuePair getDescription(@Param(value = "companyCodeId") String companyCodeId,
                                        @Param(value = "languageId") String languageId,
                                        @Param(value = "plantId") String plantId,
                                        @Param(value = "warehouseId") String warehouseId);

    //Status Description
    @Query(value = "select status_text \n" +
            "from tblstatusid \n" +
            "where \n" +
            "status_id IN (:statusId) and \n" +
            "lang_id IN (:languageId) and \n" +
            "is_deleted=0", nativeQuery = true)
    public String getStatusDescription(@Param(value = "statusId") Long statusId,
                                       @Param(value = "languageId") String languageId);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String refDocNumber, String preInboundNo, Long lineNo, String itemCode, Long deletionIndicator);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndCaseCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String refDocNumber, String preInboundNo, Long lineNo, String itemCode, String caseCode, Long deletionIndicator);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndLineNoAndItemCodeAndStatusIdInAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String preInboundNo,
            Long lineNo, String itemCode, List<Long> statusIds, Long deletionIndicator);

    Optional<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndPalletCodeAndCaseCodeAndLineNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, String stagingNo, String palletCode,
            String caseCode, Long lineNo, String itemCode, Long deletionIndicator);

    List<StagingLineEntityV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndLineNoAndItemCodeAndCaseCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, String stagingNo, Long lineNo,
            String itemCode, String caseCode, Long deletionIndicator);

    //getInventory - almailem
    @Query(value = "select * from tblinventory ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.c_id in (:companyCode) and \n" +
            "ip.plant_id in (:plantId) and \n" +
            "ip.wh_id in (:warehouseId) and \n" +
            "ip.lang_id in (:languageId) and \n" +
            "ip.mfr_code in (:manufactureCode) and \n" +
            "ip.pack_barcode in (:packBarcode) and \n" +
            "ip.is_deleted = 0", nativeQuery = true)
    public InventoryV2 getInventory(@Param(value = "itemCode") String itemCode,
                                    @Param(value = "companyCode") String companyCode,
                                    @Param(value = "plantId") String plantId,
                                    @Param(value = "warehouseId") String warehouseId,
                                    @Param(value = "manufactureCode") String manufactureCode,
                                    @Param(value = "packBarcode") String packBarcode,
                                    @Param(value = "languageId") String languageId);
}