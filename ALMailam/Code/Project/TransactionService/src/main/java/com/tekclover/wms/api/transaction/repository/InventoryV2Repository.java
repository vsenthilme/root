package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.dto.IInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryV2Repository extends PagingAndSortingRepository<InventoryV2, Long>, JpaSpecificationExecutor<InventoryV2> {


    List<InventoryV2> findByWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
            String warehouseId, String itemCode, Long binClassId, Long deletionIndicator);

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
            public IInventory findInventoryForPeriodicRun (
            @Param(value = "warehouseId") String warehouseId,
            @Param(value = "itemCode") String itemCode,
            @Param(value = "storageBin") String storageBin,
            @Param(value = "packbarCode") String packbarCode);
}