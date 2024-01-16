package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.dto.IInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.IInventoryImpl;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InventoryV2Repository extends PagingAndSortingRepository<InventoryV2, Long>,
        JpaSpecificationExecutor<InventoryV2>, StreamableJpaSpecificationRepository<InventoryV2> {


    List<InventoryV2> findByWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
            String warehouseId, String itemCode, Long binClassId, Long deletionIndicator);

    List<InventoryV2> findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerNameAndBinClassIdAndDeletionIndicator(
            String companycode, String plantId, String languageId, String warehouseId,
            String itemCode, String manufacturerName, Long binClassId, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndReferenceDocumentNoAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String referenceDocumentNo, String itemCode, Long deletionIndicator);

    InventoryV2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndStockTypeIdAndSpecialStockIndicatorIdAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String packBarcodes, String itemCode,
            String storageBin, Long stockTypeId, Long specialStockIndicatorId, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndBinClassIdAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String packBarcodes, String itemCode, Long binClassId, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String packBarcodes, String itemCode, String storageBin, String manufacturerCode, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String packBarcodes, String itemCode, String manufacturerCode, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerNameAndBinClassIdAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String itemCode, String manufacturerName, Long binClassId, Long deletionIndicator);

    @Query(value = "SELECT LANG_ID languageId, \n"
            + "C_ID companyCodeId, \n"
            + "PLANT_ID plantId, \n"
            + "WH_ID warehouseId, \n"
            + "PAL_CODE palletCode, \n"
            + "CASE_CODE caseCode, \n"
            + "PACK_BARCODE packBarcodes, \n"
            + "ITM_CODE itemCode, \n"
            + "VAR_ID variantCode, \n"
            + "VAR_SUB_ID variantSubCode, \n"
            + "STR_NO batchSerialNumber, \n"
            + "ST_BIN storageBin, \n"
            + "STCK_TYP_ID stockTypeId, \n"
            + "SP_ST_IND_ID specialStockIndicatorId, \n"
            + "REF_ORD_NO referenceOrderNo, \n"
            + "STR_MTD storageMethod, \n"
            + "BIN_CL_ID binClassId, \n"
            + "TEXT description, \n"
            + "INV_QTY inventoryQuantity, \n"
            + "ALLOC_QTY allocatedQuantity, \n"
            + "INV_UOM inventoryUom, \n"
            + "MFR_DATE manufacturerDate, \n"
            + "EXP_DATE expiryDate, \n"
            + "REF_FIELD_1 referenceField1, \n"
            + "REF_FIELD_2 referenceField2, \n"
            + "REF_FIELD_3 referenceField3, \n"
            + "COALESCE(INV_QTY,0)+COALESCE(ALLOC_QTY,0) referenceField4, \n"
            + "REF_FIELD_5 referenceField5, \n"
            + "REF_FIELD_6 referenceField6, \n"
            + "REF_FIELD_7 referenceField7, \n"
            + "REF_FIELD_8 referenceField8, \n"
            + "REF_FIELD_9 referenceField9, \n"
            + "REF_FIELD_10 referenceField10, \n"
            + "IU_CTD_BY createdBy, \n"
            + "IU_CTD_ON createdOn, \n"
            + "UTD_BY updatedBy, \n"
            + "UTD_ON updatedOn, \n"
            + "MFR_CODE manufacturerCode, \n"
            + "BARCODE_ID barcodeId, \n"
            + "CBM cbm, \n"
            + "CBM_UNIT cbmUnit, \n"
            + "CBM_PER_QTY cbmPerQuantity, \n"
            + "MFR_NAME manufacturerName, \n"
            + "ORIGIN origin, \n"
            + "BRAND brand, \n"
            + "REF_DOC_NO referenceDocumentNo, \n"
            + "C_TEXT companyDescription, \n"
            + "PLANT_TEXT plantDescription, \n"
            + "WH_TEXT warehouseDescription, \n"
            + "STATUS_TEXT statusDescription", nativeQuery = true)
    public IInventory findInventoryForPeriodicRun(
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "storageBin") String storageBin,
            @Param(value = "packbarCode") String packbarCode);

    @Query(value = "SELECT LANG_ID languageId, \n"
            + "C_ID companyCodeId, \n"
            + "PLANT_ID plantId, \n"
            + "WH_ID warehouseId, \n"
            + "PAL_CODE palletCode, \n"
            + "CASE_CODE caseCode, \n"
            + "PACK_BARCODE packBarcodes, \n"
            + "ITM_CODE itemCode, \n"
            + "VAR_ID variantCode, \n"
            + "VAR_SUB_ID variantSubCode, \n"
            + "STR_NO batchSerialNumber, \n"
            + "ST_BIN storageBin, \n"
            + "STCK_TYP_ID stockTypeId, \n"
            + "SP_ST_IND_ID specialStockIndicatorId, \n"
            + "REF_ORD_NO referenceOrderNo, \n"
            + "STR_MTD storageMethod, \n"
            + "BIN_CL_ID binClassId, \n"
            + "TEXT description, \n"
            + "sum(INV_QTY) inventoryQuantity, \n"
            + "ALLOC_QTY allocatedQuantity, \n"
            + "INV_UOM inventoryUom, \n"
            + "MFR_DATE manufacturerDate, \n"
            + "EXP_DATE expiryDate, \n"
            + "REF_FIELD_1 referenceField1, \n"
            + "REF_FIELD_2 referenceField2, \n"
            + "REF_FIELD_3 referenceField3, \n"
//            + "COALESCE(INV_QTY,0)+COALESCE(ALLOC_QTY,0) referenceField4, \n"
            + "REF_FIELD_5 referenceField5, \n"
            + "REF_FIELD_6 referenceField6, \n"
            + "REF_FIELD_7 referenceField7, \n"
            + "REF_FIELD_8 referenceField8, \n"
            + "REF_FIELD_9 referenceField9, \n"
            + "REF_FIELD_10 referenceField10, \n"
            + "IU_CTD_BY createdBy, \n"
            + "IU_CTD_ON createdOn, \n"
            + "UTD_BY updatedBy, \n"
            + "UTD_ON updatedOn, \n"
            + "MFR_CODE manufacturerCode, \n"
            + "BARCODE_ID barcodeId, \n"
            + "CBM cbm, \n"
            + "CBM_UNIT cbmUnit, \n"
            + "CBM_PER_QTY cbmPerQuantity, \n"
            + "MFR_NAME manufacturerName, \n"
            + "ORIGIN origin, \n"
            + "BRAND brand, \n"
            + "REF_DOC_NO referenceDocumentNo, \n"
            + "C_TEXT companyDescription, \n"
            + "PLANT_TEXT plantDescription, \n"
            + "WH_TEXT warehouseDescription, \n"
            + "STATUS_TEXT statusDescription \n"
            + "from tblinventory \n"
            + "WHERE itm_code in (:itemCode) and \n" +
            "wh_id in (:warehouseId) and \n" +
            "bin_cl_id in (:binClassId) and \n" +
            "plant_id in (:plantId) and \n" +
            "lang_id in (:languageId) and \n" +
            "mfr_name in (:manufacturerName) and \n" +
            "c_id in (:companyCodeId) and is_deleted = 0 \n"+
            "group by itm_code, mfr_name, st_bin,LANG_ID,C_ID,PLANT_ID,WH_ID,VAR_ID,VAR_SUB_ID,STR_NO,STCK_TYP_ID,\n"+
            "SP_ST_IND_ID,REF_ORD_NO,STR_MTD,BIN_CL_ID,TEXT,ALLOC_QTY,INV_UOM,MFR_DATE,EXP_DATE,REF_FIELD_1,REF_FIELD_2,\n"+
            "REF_FIELD_3,REF_FIELD_5,REF_FIELD_6,REF_FIELD_7,REF_FIELD_8,REF_FIELD_9,REF_FIELD_10,\n"+
            "IU_CTD_BY,IU_CTD_ON,UTD_BY,UTD_ON,MFR_CODE,BARCODE_ID,CBM,CBM_UNIT,CBM_PER_QTY,ORIGIN,BRAND,REF_DOC_NO,\n"+
            "C_TEXT,PLANT_TEXT,WH_TEXT,STATUS_TEXT,PAL_CODE,CASE_CODE,PACK_BARCODE "
            , nativeQuery = true)
    public List<IInventoryImpl> findInventoryForPerpertual(
            @Param(value = "companyCodeId") String companyCodeId,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "binClassId") Long binClassId,
            @Param(value = "manufacturerName") String manufacturerName);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String itemCode, Long binClassId, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String itemCode, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String packBarcodes, String itemCode, String storageBin, Long deletionIndicator);

    InventoryV2 findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerCodeAndPackBarcodesAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId,
            String itemCode, String manufacturerCode, String packBarcodes, Long deletionIndicator);

    //getStorageBin - almailem
    @Query(value = "select * from tblinventory ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.wh_id in (:warehouseId) and \n" +
            "ip.bin_cl_id in (:binClassId) and \n" +
            "ip.is_deleted = 0", nativeQuery = true)
    public List<InventoryV2> getInventoryStorageBinLst(@Param(value = "itemCode") String itemCode,
                                                       @Param(value = "warehouseId") String warehouseId,
                                                       @Param(value = "binClassId") Long binClassId);

    //getStorageBin - almailem
    @Query(value = "select st_bin from tblinventory ip \n" +
            "WHERE ip.itm_code in (:itemCode) and \n" +
            "ip.wh_id in (:warehouseId) and \n" +
            "ip.bin_cl_id in (:binClassId) and \n" +
            "ip.is_deleted = 0", nativeQuery = true)
    public List<String> getInventoryStorageBinList(@Param(value = "itemCode") String itemCode,
                                                   @Param(value = "warehouseId") String warehouseId,
                                                   @Param(value = "binClassId") Long binClassId);


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

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndAndStockTypeIdAndBinClassIdAndInventoryQuantityGreaterThanAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            Long stockTypeId, Long binClassId, Double invQty, Long deletionIndicator);

    @Query(value = "SELECT ST_BIN AS storageBin, SUM(INV_QTY) AS inventoryQty FROM tblinventory \r\n"
            + "WHERE C_ID = :companyCodeId and PLANT_ID = :plantId and LANG_ID = :languageId and \r\n"
            + "WH_ID = :warehouseId and ITM_CODE = :itemCode AND BIN_CL_ID = 1 AND STCK_TYP_ID = 1 \r\n"
            + "AND REF_FIELD_10 IN :storageSecIds AND IS_DELETED = 0 \r\n"
            + "GROUP BY ST_BIN \r\n"
            + "HAVING SUM(INV_QTY) > 0 \r\n"
            + "ORDER BY ST_BIN, SUM(INV_QTY)", nativeQuery = true)
    public List<IInventory> findInventoryGroupByStorageBinV2(
            @Param(value = "companyCodeId") String companyCodeId,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "storageSecIds") List<String> storageSecIds);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndBinClassIdAndStorageBinAndStockTypeIdAndDeletionIndicatorAndInventoryQuantityGreaterThanOrderByInventoryQuantity(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String itemCode, Long binClassId, String storageBin, Long stockTypeId, long l, double m);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndReferenceField10InAndBinClassIdAndInventoryQuantityGreaterThan(
            String languageId, String companyCode, String plantId, String warehouseId, String itemCode,
            List<String> storageSectionIds, long l, double m);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndReferenceField10InAndStockTypeIdAndBinClassIdAndInventoryQuantityGreaterThan(
            String languageId, String companyCode, String plantId, String warehouseId, String itemCode,
            List<String> storageSectionIds, Long stockTypeId, long l, double m);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndStorageBinAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String storageBin, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndPackBarcodesAndBinClassIdAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String itemCode, String packBarcodes, Long binClassId, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndAndStockTypeIdAndBinClassIdAndManufacturerCodeAndInventoryQuantityGreaterThanAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            Long stockTypeId, Long binClassId, String manufacturerCode, Double invQty, Long deletionIndicator);

    InventoryV2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndPackBarcodesAndReferenceDocumentNoAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            String pickedPackCode, String refDocNumber, String manufacturerCode, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String packBarcodes, String itemCode, Long deletionIndicator);

    List<InventoryV2> findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
            String companycode, String plantId, String languageId, String warehouseId,
            String itemCode, Long binClassId, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndReferenceDocumentNoAndStockTypeIdAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String referenceDocumentNo, Long stockTypeId, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndAndStockTypeIdAndBinClassIdAndManufacturerNameAndInventoryQuantityGreaterThanAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            Long stockTypeId, Long binClassId, String manufacturerName, Double invQty, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndManufacturerNameAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String packBarcodes,
            String itemCode, String storageBin, String manufacturerName, Long deletionIndicator);

    InventoryV2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndPackBarcodesAndReferenceDocumentNoAndManufacturerNameAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String itemCode, String pickedPackCode, String refDocNumber, String manufacturerName, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerNameAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String itemCode, String manufacturerName, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndManufacturerNameAndStorageBinAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String packBarcodes, String itemCode, String manufacturerName, String storageBin, Long deletionIndicator);

    Optional<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndManufacturerCodeAndStorageBinAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String packBarcodes,
            String itemCode, String manufacturerName, String storageBin, Long deletionIndicator);

    List<InventoryV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerCodeAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, String itemCode, String manufacturerName, Long deletionIndicator);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE tblinventory iv SET iv.STCK_TYP_ID = 1 \n" +
//            "WHERE iv.warehouseId = :warehouseId AND iv.referenceDocumentNo = :referenceDocumentNo and \n" +
//            "iv.companyCode = :companyCode and iv.plantId = :plantId and iv.languageId = :languageId and iv.stockTypeId = :stockTypeId")
//    void updateInventoryStockTypeId(@Param("warehouseId") String warehouseId,
//                                   @Param("companyCode") String companyCode,
//                                   @Param("plantId") String plantId,
//                                   @Param("languageId") String languageId,
//                                   @Param("referenceDocumentNo") String referenceDocumentNo,
//                                   @Param("stockTypeId") Long stockTypeId);

    @Query(value = "select LANG_ID languageId,\n" +
            "C_ID companyCodeId,\n" +
            "PLANT_ID plantId,\n" +
            "WH_ID warehouseId,\n" +
            "PAL_CODE palletCode,\n" +
            "CASE_CODE caseCode,\n" +
            "PACK_BARCODE packBarcodes,\n" +
            "ITM_CODE itemCode,\n" +
            "VAR_ID variantCode,\n" +
            "VAR_SUB_ID variantSubCode,\n" +
            "STR_NO batchSerialNumber,\n" +
            "ST_BIN storageBin,\n" +
            "STCK_TYP_ID stockTypeId,\n" +
            "SP_ST_IND_ID specialStockIndicatorId,\n" +
            "REF_ORD_NO referenceOrderNo,\n" +
            "STR_MTD storageMethod,\n" +
            "BIN_CL_ID binClassId,\n" +
            "TEXT description,\n" +
            "INV_QTY inventoryQuantity,\n" +
            "ALLOC_QTY allocatedQuantity,\n" +
            "INV_UOM inventoryUom,\n" +
            "MFR_DATE manufacturer,\n" +
            "EXP_DATE expiry,\n" +
            "IS_DELETED deletionIndicator,\n" +
            "REF_FIELD_1 referenceField1,\n" +
            "REF_FIELD_2 referenceField2,\n" +
            "REF_FIELD_3 referenceField3,\n" +
            "REF_FIELD_4 referenceField4,\n" +
            "REF_FIELD_5 referenceField5,\n" +
            "REF_FIELD_6 referenceField6,\n" +
            "REF_FIELD_7 referenceField7,\n" +
            "REF_FIELD_8 referenceField8,\n" +
            "REF_FIELD_9 referenceField9,\n" +
            "REF_FIELD_10 referenceField10,\n" +
            "IU_CTD_BY createdBy,\n" +
            "IU_CTD_ON createdOn,\n" +
            "FORMAT(IU_CTD_ON,'dd-MM-yyyy hh:mm:ss') sCreatedOn,\n" +
            "UTD_BY updatedBy,\n" +
            "UTD_ON updatedOn,\n" +
            "MFR_CODE manufacturerCode,\n" +
            "BARCODE_ID barcodeId,\n" +
            "CBM cbm,\n" +
            "level_id levelId,\n" +
            "CBM_UNIT cbmUnit,\n" +
            "CBM_PER_QTY cbmPerQuantity,\n" +
            "MFR_NAME manufacturerName,\n" +
            "ORIGIN origin,\n" +
            "BRAND brand,\n" +
            "REF_DOC_NO referenceDocumentNo,\n" +
            "C_TEXT companyDescription,\n" +
            "PLANT_TEXT plantDescription,\n" +
            "WH_TEXT warehouseDescription,\n" +
            "STATUS_TEXT statusDescription\n" +
            "from tblinventory \n" +
            "where \n" +
            "(COALESCE(:companyCodeId, null) IS NULL OR (c_id IN (:companyCodeId))) and \n" +
            "(COALESCE(:languageId, null) IS NULL OR (lang_id IN (:languageId))) and \n" +
            "(COALESCE(:plantId, null) IS NULL OR (plant_id IN (:plantId))) and \n" +
            "(COALESCE(:warehouseId, null) IS NULL OR (wh_id IN (:warehouseId))) and \n" +
            "(COALESCE(:referenceDocumentNo, null) IS NULL OR (ref_doc_no IN (:referenceDocumentNo))) and \n" +
            "(COALESCE(:barcodeId, null) IS NULL OR (BARCODE_ID IN (:barcodeId))) and \n" +
            "(COALESCE(:manufacturerCode, null) IS NULL OR (MFR_CODE IN (:manufacturerCode))) and \n" +
            "(COALESCE(:packBarcodes, null) IS NULL OR (PACK_BARCODE IN (:packBarcodes))) and \n" +
            "(COALESCE(:itemCode, null) IS NULL OR (ITM_CODE IN (:itemCode))) and \n" +
            "(COALESCE(:storageBin, null) IS NULL OR (ST_BIN IN (:storageBin))) and\n" +
            "(COALESCE(:stockTypeId, null) IS NULL OR (STCK_TYP_ID IN (:stockTypeId))) and \n" +
            "(COALESCE(:specialStockIndicatorId, null) IS NULL OR (SP_ST_IND_ID IN (:specialStockIndicatorId))) and \n" +
            "(COALESCE(:binClassId, null) IS NULL OR (BIN_CL_ID IN (:binClassId))) and\n" +
            "(COALESCE(:description, null) IS NULL OR (TEXT IN (:description))) \n"
            , nativeQuery = true)
    public List<IInventoryImpl> findInventory(@Param("companyCodeId") List<String> companyCodeId,
                                              @Param("languageId") List<String> languageId,
                                              @Param("plantId") List<String> plantId,
                                              @Param("warehouseId") List<String> warehouseId,
                                              @Param("referenceDocumentNo") List<String> referenceDocumentNo,
                                              @Param("barcodeId") List<String> barcodeId,
                                              @Param("manufacturerCode") List<String> manufacturerCode,
                                              @Param("packBarcodes") List<String> packBarcodes,
                                              @Param("itemCode") List<String> itemCode,
                                              @Param("storageBin") List<String> storageBin,
                                              @Param("description") String description,
                                              @Param("stockTypeId") List<Long> stockTypeId,
                                              @Param("specialStockIndicatorId") List<Long> specialStockIndicatorId,
                                              @Param("binClassId") List<Long> binClassId);


    Page<InventoryV2> findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, Long deletionIndicator, Pageable pageable);

    // SQL Query for getting Inventory
    @Query (value = "SELECT INV_QTY AS inventoryQty, INV_UOM AS inventoryUom FROM tblinventory "
            + "WHERE C_ID = :companyCodeId AND PLANT_ID = :plantId AND LANG_ID = :languageId AND WH_ID = :warehouseId AND PACK_BARCODE = :packbarCode AND "
            + "ITM_CODE = :itemCode AND MFR_NAME = :manufacturerName AND ST_BIN = :storageBin", nativeQuery = true)
    public IInventory findInventoryForPeriodicRunV2 (
            @Param(value = "companyCodeId") String companyCodeId,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "manufacturerName") String manufacturerName,
            @Param(value = "storageBin") String storageBin,
            @Param(value = "packbarCode") String packbarCode);

    @Query (value = "SELECT SUM(INV_QTY) FROM tblinventory \r\n"
            + " WHERE C_ID = :companyCodeId AND PLANT_ID = :plantId AND LANG_ID = :languageId AND WH_ID = :warehouseId AND MFR_NAME = :manufacturerName AND ITM_CODE = :itemCode AND \r\n"
            + " BIN_CL_ID = 1 \r\n"
            + " GROUP BY ITM_CODE", nativeQuery = true)
    public Double getInventoryQtyCountV2 (
            @Param(value = "companyCodeId") String companyCodeId,
            @Param(value = "plantId") String plantId,
            @Param(value = "languageId") String languageId,
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "manufacturerName") String manufacturerName,
            @Param(value = "itemCode") String itemCode);
}