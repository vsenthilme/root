package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PerpetualLine;
import com.mnrclara.spark.core.model.wmscore.SearchPerpetualLine;
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
public class PerpetualLineServiceV2 {
    Properties conProp = new Properties();
    SparkSession sparkSession = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PerpetualLineServiceV2() throws ParseException {
        // Connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");

        // Initialize Spark session and read data from SQL table
        sparkSession = SparkSession.builder().master("local[*]").appName("PerpetualLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblperpetualline", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblperpetuallinev3");
    }

    /**
     *
     * @param searchPerpetualLine
     * @return
     * @throws ParseException
     */
    public List<PerpetualLine> findPerpetualLineV3(SearchPerpetualLine searchPerpetualLine) throws ParseException {


        Dataset<Row> perpetualLineQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "CC_NO as cycleCountNo, "
                + "ST_BIN as storageBin, "
                + "ITM_CODE as itemCode, "
                + "PACK_BARCODE as packBarcodes, "
                + "ITM_DESC as itemDesc, "
                + "MFR_PART as manufacturerPartNo, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "STR_NO as batchSerialNumber, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicator, "
                + "ST_SEC_ID as storageSectionId, "
                + "INV_QTY as inventoryQuantity, "
                + "INV_UOM as inventoryUom, "
                + "CTD_QTY as countedQty, "
                + "VAR_QTY as varianceQty, "
                + "COUNTER_ID as cycleCounterId, "
                + "COUNTER_NM as cycleCounterName, "
                + "STATUS_ID as statusId, "
                + "ACTION as cycleCountAction, "
                + "REF_NO as referenceNo, "
                + "APP_PROCESS_ID as approvalProcessId, "
                + "APP_LVL as approvalLevel, "
                + "APP_CODE as approverCode, "
                + "APP_STATUS as approvalStatus, "
                + "REMARK as remarks, "
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
                + "CC_CTD_BY as createdBy, "
                + "CC_CTD_ON as createdOn, "
                + "CC_CNF_BY as confirmedBy, "
                + "CC_CNF_ON as confirmedOn, "
                + "CC_CNT_BY as countedBy, "
                + "CC_CNT_ON as countedOn "
                + "From tblperpetuallinev3 where IS_DELETED = 0 ");

        perpetualLineQueryV3.cache();

        if (searchPerpetualLine.getLanguageId() != null && !searchPerpetualLine.getLanguageId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("LANG_ID").isin(searchPerpetualLine.getLanguageId().toArray()));
        }
        if (searchPerpetualLine.getCompanyCodeId() != null && !searchPerpetualLine.getCompanyCodeId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("C_ID").isin(searchPerpetualLine.getCompanyCodeId().toArray()));
        }
        if (searchPerpetualLine.getPlantId() != null && !searchPerpetualLine.getPlantId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("PLANT_ID").isin(searchPerpetualLine.getPlantId().toArray()));
        }
        if (searchPerpetualLine.getWarehouseId() != null && !searchPerpetualLine.getWarehouseId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("WH_ID").isin(searchPerpetualLine.getWarehouseId().toArray()));
        }
        if (searchPerpetualLine.getCycleCountNo() != null && !searchPerpetualLine.getCycleCountNo().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("CC_NO").isin(searchPerpetualLine.getCycleCountNo().toArray()));
        }
        if (searchPerpetualLine.getCycleCounterId() != null && !searchPerpetualLine.getCycleCounterId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("COUNTER_ID").isin(searchPerpetualLine.getCycleCounterId().toArray()));
        }
        if (searchPerpetualLine.getItemCode() != null && !searchPerpetualLine.getItemCode().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("ITM_DESC").isin(searchPerpetualLine.getItemCode().toArray()));
        }
        if (searchPerpetualLine.getStorageBin() != null && !searchPerpetualLine.getStorageBin().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("ST_BIN").isin(searchPerpetualLine.getStorageBin().toArray()));
        }
        if (searchPerpetualLine.getPackBarcodes() != null && !searchPerpetualLine.getPackBarcodes().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("PACK_BARCODE").isin(searchPerpetualLine.getPackBarcodes().toArray()));
        }
        if (searchPerpetualLine.getManufacturerPartNo() != null && !searchPerpetualLine.getManufacturerPartNo().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("MFR_PART").isin(searchPerpetualLine.getManufacturerPartNo().toArray()));
        }
        if (searchPerpetualLine.getStorageSectionId() != null && !searchPerpetualLine.getStorageSectionId().isEmpty()) {
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("ST_SEC_ID").isin(searchPerpetualLine.getStorageSectionId().toArray()));
        }
        if (searchPerpetualLine.getLineStatusId() != null && !searchPerpetualLine.getLineStatusId().isEmpty()) {
            List<String> lineStatusIdString = searchPerpetualLine.getLineStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("STATUS_ID").isin(lineStatusIdString.toArray()));
        }
        if (searchPerpetualLine.getStockTypeId() != null && !searchPerpetualLine.getStockTypeId().isEmpty()) {
            List<String> stockTypeIdString = searchPerpetualLine.getStockTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("STCK_TYP_ID").isin(stockTypeIdString.toArray()));
        }
        if(searchPerpetualLine.getStartCreatedOn() != null){
            Date fromCreatedOn = searchPerpetualLine.getStartCreatedOn();
            fromCreatedOn = DateUtils.truncate(fromCreatedOn, Calendar.DAY_OF_MONTH);
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("CC_CTD_ON").$greater$eq(dateFormat.format(fromCreatedOn)));
        }
        if(searchPerpetualLine.getEndCreatedOn() != null){
            Date toCreatedOn = searchPerpetualLine.getEndCreatedOn();
            toCreatedOn = DateUtils.ceiling(toCreatedOn, Calendar.DAY_OF_MONTH);
            perpetualLineQueryV3 = perpetualLineQueryV3.filter(col("CC_CTD_ON").$less$eq(dateFormat.format(toCreatedOn)));
        }


        Encoder<PerpetualLine> perpetualLineEncoder = Encoders.bean(PerpetualLine.class);
        Dataset<PerpetualLine> dataSetPerpetualLine = perpetualLineQueryV3.as(perpetualLineEncoder);
        List<PerpetualLine> results = dataSetPerpetualLine.collectAsList();
        return results;
    }
}
