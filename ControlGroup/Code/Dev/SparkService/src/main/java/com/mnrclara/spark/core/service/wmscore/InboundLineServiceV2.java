package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.InboundLine;
import com.mnrclara.spark.core.model.wmscore.SearchInboundLine;
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
public class InboundLineServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InboundLineServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("InboundLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblinboundline", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinboundlinev3");
    }

    public List<InboundLine> findInboundLineV3(SearchInboundLine searchInBoundLine) throws ParseException {

        Dataset<Row> inboundLineQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "REF_DOC_NO as refDocNumber, "
                + "PRE_IB_NO as preInboundNo, "
                + "IB_LINE_NO as lineNo, "
                + "ITM_CODE as itemCode, "
                + "ORD_QTY as orderedQuantity, "
                + "ORD_UOM as orderedUnitOfMeasure, "
                + "ACCEPT_QTY as acceptedQty, "
                + "DAMAGE_QTY as damageQty, "
                + "PA_CNF_QTY as putawayConfirmedQty, "
                + "VAR_QTY as varianceQty, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "REF_ORD_NO as referenceOrderNo, "
                + "STATUS_ID as statusId, "
                + "PARTNER_CODE as vendorCode, "
                + "EA_DATE as expectedArrivalDate, "
                + "CONT_NO as containerNo, "
                + "INV_NO as invoiceNo, "
                + "TEXT as description, "
                + "MFR_PART as manufacturerPartNo, "
                + "HSN_CODE as hsnCode, "
                + "ITM_BARCODE as itemBarcode, "
                + "ITM_CASE_QTY as itemCaseQty, "
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
                + "CTD_BY as createdBy, "
                + "CTD_ON as createdOn, "
                + "UTD_BY as updatedBy, "
                + "UTD_ON as updatedOn, "
                + "IB_CNF_BY as confirmedBy, "
                + "IB_CNF_ON as confirmedOn "
                + "From tblinboundlinev3 where IS_DELETED = 0 "
        );
        inboundLineQueryV3.cache();

        if (searchInBoundLine.getWarehouseId() != null && !searchInBoundLine.getWarehouseId().isEmpty()) {
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("WH_ID").isin(searchInBoundLine.getWarehouseId().toArray()));
        }
        if (searchInBoundLine.getReferenceField1() != null && !searchInBoundLine.getReferenceField1().isEmpty()) {
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("REF_FIELD_1").isin(searchInBoundLine.getReferenceField1().toArray()));
        }
        if (searchInBoundLine.getStatusId() != null && !searchInBoundLine.getStatusId().isEmpty()) {
            List<String> headerStatusIdStrings = searchInBoundLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("STATUS_ID").isin(headerStatusIdStrings.toArray()));
        }

        if (searchInBoundLine.getStartConfirmedOn() != null) {
            Date startDate = searchInBoundLine.getStartConfirmedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("IB_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInBoundLine.getEndConfirmedOn() != null) {
            Date endDate = searchInBoundLine.getEndConfirmedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("IB_CNF_ON").$greater$eq(dateFormat.format(endDate)));
        }

        if (searchInBoundLine.getLanguageId() != null && !searchInBoundLine.getLanguageId().isEmpty()) {
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("LANG_ID").isin(searchInBoundLine.getLanguageId().toArray()));
        }
        if (searchInBoundLine.getCompanyCode() != null && !searchInBoundLine.getCompanyCode().isEmpty()) {
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("C_ID").isin(searchInBoundLine.getCompanyCode().toArray()));
        }
        if (searchInBoundLine.getPlantId() != null && !searchInBoundLine.getPlantId().isEmpty()) {
            inboundLineQueryV3 = inboundLineQueryV3.filter(col("PLANT_ID").isin(searchInBoundLine.getPlantId().toArray()));
        }


        Encoder<InboundLine> inboundLineEncoder = Encoders.bean(InboundLine.class);
        Dataset<InboundLine> datasetControlGroup = inboundLineQueryV3.as(inboundLineEncoder);
        List<InboundLine> results = datasetControlGroup.collectAsList();
        return results;
    }
}
