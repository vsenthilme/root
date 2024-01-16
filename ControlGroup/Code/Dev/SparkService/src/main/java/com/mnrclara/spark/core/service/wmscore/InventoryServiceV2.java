package com.mnrclara.spark.core.service.wmscore;

import com.mnrclara.spark.core.model.wmscore.Inventory;
import com.mnrclara.spark.core.model.wmscore.SearchInventory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Service
@Slf4j
public class InventoryServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;

    public InventoryServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("Inventory.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS_ALMDEV", "tblinventory", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinventoryv3");
    }

    public List<Inventory> findInventoryV3(SearchInventory searchInventory) throws ParseException {

        Dataset<Row> inventoryQueryV3 = sparkSession.sql("Select "
                + "INV_ID as inventoryId, "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PAL_CODE as palletCode, "
                + "CASE_CODE as caseCode, "
                + "PACK_BARCODE as packBarcodes, "
                + "ITM_CODE as itemCode, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "STR_NO as batchSerialNumber, "
                + "ST_BIN as storageBin, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "REF_ORD_NO as referenceOrderNo, "
                + "STR_MTD as storageMethod, "
                + "BIN_CL_ID as binClassId, "
                + "TEXT as description, "
                + "INV_QTY as inventoryQuantity, "
                + "ALLOC_QTY as allocatedQuantity, "
                + "INV_UOM as inventoryUom, "
                + "MFR_DATE as manufacturerDate, "
                + "EXP_DATE as expiryDate, "
                + "IS_DELETED as deletionIndicator, "
                + "REF_FIELD_1 as referenceField1, "
                + "REF_FIELD_2 as referenceField2, "
                + "REF_FIELD_3 as referenceField3, "
                + "REF_FIELD_4 as referenceField4, "
                + "REF_FIELD_5 as referenceField5, "
                + "REF_FIELD_6 as referenceField6, "
                + "REF_FIELD_7 as referenceField7, "
                + "REF_FIELD_8 as referenceField8, "
                + "REF_FIELD_9 as referenceField9, "
                + "REF_FIELD_10 as referenceField10, "
                + "IU_CTD_BY as createdBy, "
                + "IU_CTD_ON as createdOn, "
                + "UTD_BY as updatedBy, "
                + "UTD_ON as updatedOn "
                + "From tblinventoryv3 Where IS_DELETED = 0 "
        );
        inventoryQueryV3.cache();

        if (searchInventory.getWarehouseId() != null && !searchInventory.getWarehouseId().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("WH_ID").isin(searchInventory.getWarehouseId().toArray()));
        }
        if (searchInventory.getCompanyCodeId() != null && !searchInventory.getCompanyCodeId().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("C_ID").isin(searchInventory.getCompanyCodeId().toArray()));
        }
        if (searchInventory.getPlantId() != null && !searchInventory.getPlantId().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("PLANT_ID").isin(searchInventory.getPlantId().toArray()));
        }
        if (searchInventory.getLanguageId() != null && !searchInventory.getLanguageId().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("LANG_ID").isin(searchInventory.getLanguageId().toArray()));
        }
        if (searchInventory.getPackBarcodes() != null && !searchInventory.getPackBarcodes().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("PACK_BARCODE").isin(searchInventory.getPackBarcodes().toArray()));
        }
        if (searchInventory.getItemCode() != null && !searchInventory.getItemCode().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("ITM_CODE").isin(searchInventory.getItemCode().toArray()));
        }
        if (searchInventory.getStorageBin() != null && !searchInventory.getStorageBin().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("ST_BIN").isin(searchInventory.getStorageBin().toArray()));
        }
        if (searchInventory.getStorageSectionId() != null && !searchInventory.getStorageSectionId().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("REF_FIELD_10").isin(searchInventory.getStorageSectionId().toArray()));
        }
        if (searchInventory.getStockTypeId() != null && !searchInventory.getStockTypeId().isEmpty()) {
            List<String> stockTypeIdString = searchInventory.getStockTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            inventoryQueryV3 = inventoryQueryV3.filter(col("STCK_TYP_ID").isin(stockTypeIdString.toArray()));
        }
        if (searchInventory.getSpecialStockIndicatorId() != null && !searchInventory.getSpecialStockIndicatorId().isEmpty()) {
            List<String>specialStockString = searchInventory.getSpecialStockIndicatorId().stream().map(String::valueOf).collect(Collectors.toList());
            inventoryQueryV3 = inventoryQueryV3.filter(col("SP_ST_IND_ID").isin(specialStockString.toArray()));
        }
        if (searchInventory.getBinClassId() != null && !searchInventory.getBinClassId().isEmpty()) {
            List<String> binClassIdString = searchInventory.getBinClassId().stream().map(String::valueOf).collect(Collectors.toList());
            inventoryQueryV3 = inventoryQueryV3.filter(col("BIN_CL_ID").isin(binClassIdString.toArray()));
        }
        if (searchInventory.getDescription() != null && !searchInventory.getDescription().isEmpty()) {
            inventoryQueryV3 = inventoryQueryV3.filter(col("TEXT").isin(searchInventory.getDescription().toArray()));
        }

        Encoder<Inventory> inventoryEncoder = Encoders.bean(Inventory.class);
        Dataset<Inventory> datasetControlGroup = inventoryQueryV3.as(inventoryEncoder);
        List<Inventory> results = datasetControlGroup.collectAsList();
        return results;
    }

}
