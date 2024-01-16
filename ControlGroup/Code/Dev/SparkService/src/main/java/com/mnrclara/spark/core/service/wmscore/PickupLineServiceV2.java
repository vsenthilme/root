package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PickupLine;
import com.mnrclara.spark.core.model.wmscore.SearchPickupLine;
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
public class PickupLineServiceV2 {
    Properties conProp = new Properties();
    SparkSession sparkSession = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PickupLineServiceV2() throws ParseException {
        // Connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");

        // Initialize Spark session and read data from SQL table
        sparkSession = SparkSession.builder().master("local[*]").appName("PickupLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblpickupline", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblpickuplinev3");
    }

    /**
     *
     * @param searchPickupLine
     * @return
     * @throws ParseException
     */
    public List<PickupLine> findPickupLineV3(SearchPickupLine searchPickupLine) throws ParseException {


        Dataset<Row> pickupLineSqlQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_OB_NO as preOutboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "OB_LINE_NO as lineNumber, "
                + "PU_NO as pickupNumber, "
                + "ITM_CODE as itemCode, "
                + "PICK_HE_NO as actualHeNo, "
                + "PICK_ST_BIN as pickedStorageBin, "
                + "PICK_PACK_BARCODE as pickedPackCode, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "STR_NO as batchSerialNumber, "
                + "PICK_CNF_QTY as pickConfirmQty, "
                + "ALLOC_QTY as allocatedQty, "
                + "PICK_UOM as pickUom, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "ITEM_TEXT as description, "
                + "MFR_PART as manufacturerPartNo, "
                + "ASS_PICKER_ID as assignedPickerId, "
                + "PICK_PAL_CODE as pickPalletCode, "
                + "PICK_CASE_CODE as pickCaseCode, "
                + "STATUS_ID as statusId, "
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
                + "PICK_CTD_BY as pickupCreatedBy, "
                + "PICK_CTD_ON as pickupCreatedOn, "
                + "PICK_UTD_BY as pickupUpdatedBy, "
                + "PICK_UTD_ON as pickupUpdatedOn, "
                + "PICK_CNF_BY as pickupConfirmedBy, "
                + "PICK_CNF_ON as pickupConfirmedOn, "
                + "PICK_REV_BY as pickupReversedBy, "
                + "PICK_REV_ON as pickupReversedOn "
                + "FROM tblpickuplinev3 WHERE IS_DELETED = 0 ");

        // Cache the DataFrame
        pickupLineSqlQueryV3.cache();


        if (searchPickupLine.getLanguageId() != null && !searchPickupLine.getLanguageId().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("LANG_ID").isin(searchPickupLine.getLanguageId().toArray()));
        }
        if (searchPickupLine.getCompanyCodeId() != null && !searchPickupLine.getCompanyCodeId().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("C_ID").isin(searchPickupLine.getCompanyCodeId().toArray()));
        }
        if (searchPickupLine.getPlantId() != null && !searchPickupLine.getPlantId().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PLANT_ID").isin(searchPickupLine.getPlantId().toArray()));
        }
        if (searchPickupLine.getWarehouseId() != null && !searchPickupLine.getWarehouseId().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("WH_ID").isin(searchPickupLine.getWarehouseId().toArray()));
        }
        if (searchPickupLine.getPreOutboundNo() != null && !searchPickupLine.getPreOutboundNo().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PRE_OB_NO").isin(searchPickupLine.getPreOutboundNo().toArray()));
        }
        if (searchPickupLine.getRefDocNumber() != null && !searchPickupLine.getRefDocNumber().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("REF_DOC_NO").isin(searchPickupLine.getRefDocNumber().toArray()));
        }
        if (searchPickupLine.getPartnerCode() != null && !searchPickupLine.getPartnerCode().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PARTNER_CODE").isin(searchPickupLine.getPartnerCode().toArray()));
        }
        if (searchPickupLine.getLineNumber() != null && !searchPickupLine.getLineNumber().isEmpty()) {
            List<String> lineNumberString = searchPickupLine.getLineNumber().stream().map(String::valueOf).collect(Collectors.toList());
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("OB_LINE_NO").isin(lineNumberString.toArray()));
        }
        if (searchPickupLine.getItemCode() != null && !searchPickupLine.getItemCode().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("ITM_CODE").isin(searchPickupLine.getItemCode().toArray()));
        }
        if (searchPickupLine.getActualHeNo() != null && !searchPickupLine.getActualHeNo().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PICK_HE_NO").isin(searchPickupLine.getActualHeNo().toArray()));
        }
        if (searchPickupLine.getPickedStorageBin() != null && !searchPickupLine.getPickedStorageBin().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PICK_ST_BIN").isin(searchPickupLine.getPickedStorageBin().toArray()));
        }
        if (searchPickupLine.getPickedPackCode() != null && !searchPickupLine.getPickedPackCode().isEmpty()) {
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PICK_PACK_BARCODE").isin(searchPickupLine.getPickedPackCode().toArray()));
        }
        if (searchPickupLine.getStatusId() != null && !searchPickupLine.getStatusId().isEmpty()) {
            List<String> statusIdString = searchPickupLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("STATUS_ID").isin(statusIdString.toArray()));
        }
        if (searchPickupLine.getFromPickConfirmedOn() != null) {
            Date startDate = searchPickupLine.getFromPickConfirmedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PICK_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchPickupLine.getToPickConfirmedOn() != null) {
            Date endDate = searchPickupLine.getToPickConfirmedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            pickupLineSqlQueryV3 = pickupLineSqlQueryV3.filter(col("PICK_CNF_ON").$less$eq(dateFormat.format(endDate)));
        }

        // Encode the result as PickupLine objects
        Encoder<PickupLine> pickupLineEncoder = Encoders.bean(PickupLine.class);
        Dataset<PickupLine> dataSet = pickupLineSqlQueryV3.as(pickupLineEncoder);
        List<PickupLine> result = dataSet.collectAsList();

        return result;
    }
}
