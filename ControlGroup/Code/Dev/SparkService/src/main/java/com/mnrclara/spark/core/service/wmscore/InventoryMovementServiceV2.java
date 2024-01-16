package com.mnrclara.spark.core.service.wmscore;

import com.mnrclara.spark.core.model.Almailem.InventoryMovementV2;
import com.mnrclara.spark.core.model.Almailem.SearchInventoryMovementV2;
import com.mnrclara.spark.core.model.wmscore.InventoryMovement;
import com.mnrclara.spark.core.model.wmscore.SearchInventoryMovement;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Service
@Slf4j
public class InventoryMovementServiceV2 {
    Properties conProp = new Properties();

    SparkSession sparkSession = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InventoryMovementServiceV2() throws ParseException {
        //connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("InventoryMovement.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblinventorymovement", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinventorymovementv3");

    }


    /**
     * @param searchInventoryMovement
     * @return
     * @throws ParseException
     */
    public List<InventoryMovement> findInventoryMovementV3(SearchInventoryMovement searchInventoryMovement) throws ParseException {

        Dataset<Row> inventoryMovementQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "MVT_TYP_ID as movementType, "
                + "SUB_MVT_TYP_ID as submovementType, "
                + "PAL_CODE as palletCode, "
                + "CASE_CODE as caseCode, "
                + "PACK_BARCODE as packBarcodes, "
                + "ITM_CODE as itemCode, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "STR_NO as batchSerialNumber, "
                + "MVT_DOC_NO as movementDocumentNo, "
                + "MFR_PART as manufacturerPartNo, "
                + "ST_BIN as storageBin, "
                + "STR_MTD as storageMethod, "
                + "TEXT as description, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicator, "
                + "MVT_QTY_VAL as movementQtyValue, "
                + "MVT_QTY as movementQty, "
                + "BAL_OH_QTY as balanceOHQty, "
                + "MVT_UOM as inventoryUom, "
                + "REF_DOC_NO as refDocNumber, "
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
                + "IS_DELETED as deletionIndicator, "
                + "IM_CTD_BY as createdBy, "
                + "IM_CTD_ON as createdOn, "
                + "COMPANY_DESC as companyDescription, "
                + "PLANT_DESC as plantDescription, "
                + "WAREHOUSE_DESC as warehouseDescription "
                + "FROM tblinventorymovementv3" +
//                "JOIN tblcompanyid cDesc ON imv.C_ID = cDesc.C_ID AND imv.LANG_ID = cDesc.LANG_ID " +
//                "JOIN tblplantid pDesc ON imv.C_ID = pDesc.C_ID AND imv.LANG_ID = pDesc.LANG_ID AND imv.PLANT_ID = pDesc.PLANT_ID " +
//                "JOIN tblwarehouseid wDesc ON imv.C_ID = wDesc.C_ID AND imv.LANG_ID = wDesc.LANG_ID AND imv.WH_ID = wDesc.WH_ID " +
                " WHERE IS_DELETED = 0");

        inventoryMovementQueryV3.cache();

        if (searchInventoryMovement.getWarehouseId() != null && !searchInventoryMovement.getWarehouseId().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("WH_ID").isin(searchInventoryMovement.getWarehouseId().toArray()));
        }
        if (searchInventoryMovement.getLanguageId() != null && !searchInventoryMovement.getLanguageId().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("LANG_ID").isin(searchInventoryMovement.getLanguageId().toArray()));
        }
        if (searchInventoryMovement.getCompanyCodeId() != null && !searchInventoryMovement.getCompanyCodeId().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("C_ID").isin(searchInventoryMovement.getCompanyCodeId().toArray()));
        }
        if (searchInventoryMovement.getPlantId() != null && !searchInventoryMovement.getPlantId().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("PLANT_ID").isin(searchInventoryMovement.getPlantId().toArray()));
        }
        if (searchInventoryMovement.getItemCode() != null && !searchInventoryMovement.getItemCode().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("ITM_CODE").isin(searchInventoryMovement.getItemCode().toArray()));
        }
        if (searchInventoryMovement.getMovementType() != null && !searchInventoryMovement.getMovementType().isEmpty()) {
            List<String> movementTypeString = searchInventoryMovement.getMovementType().stream().map(String::valueOf).collect(Collectors.toList());
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("MVT_TYP_ID").isin(movementTypeString.toArray()));
        }
        if (searchInventoryMovement.getSubmovementType() != null && !searchInventoryMovement.getSubmovementType().isEmpty()) {
            List<String> subMovementTypeString = searchInventoryMovement.getSubmovementType().stream().map(String::valueOf).collect(Collectors.toList());
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("SUB_MVT_TYP_ID").isin(subMovementTypeString.toArray()));
        }
        if (searchInventoryMovement.getPackBarcodes() != null && !searchInventoryMovement.getPackBarcodes().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("PACK_BARCODE").isin(searchInventoryMovement.getPackBarcodes().toArray()));
        }
        if (searchInventoryMovement.getBatchSerialNumber() != null && !searchInventoryMovement.getBatchSerialNumber().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("STR_NO").isin(searchInventoryMovement.getBatchSerialNumber().toArray()));
        }
        if (searchInventoryMovement.getMovementDocumentNo() != null && !searchInventoryMovement.getMovementDocumentNo().isEmpty()) {
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("MVT_DOC_NO").isin(searchInventoryMovement.getMovementDocumentNo().toArray()));
        }
        if (searchInventoryMovement.getFromCreatedOn() != null) {
            Date startDate = searchInventoryMovement.getFromCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("IM_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInventoryMovement.getToCreatedOn() != null) {
            Date endDate = searchInventoryMovement.getToCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inventoryMovementQueryV3 = inventoryMovementQueryV3.filter(col("IM_CTD_ON").$less$eq(dateFormat.format(endDate)));
        }

        Encoder<InventoryMovement> inventoryMovementEncoder = Encoders.bean(InventoryMovement.class);
        Dataset<InventoryMovement> dataSetControlGroup = inventoryMovementQueryV3.as(inventoryMovementEncoder);
        List<InventoryMovement> result = dataSetControlGroup.collectAsList();
        return result;
    }
}
