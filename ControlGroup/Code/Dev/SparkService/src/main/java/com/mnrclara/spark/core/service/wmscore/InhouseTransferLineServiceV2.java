package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.InhouseTransferLine;
import com.mnrclara.spark.core.model.wmscore.SearchInhouseTransferLine;
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
public class InhouseTransferLineServiceV2 {

    Properties conProp = new Properties();
    SparkSession sparkSession = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InhouseTransferLineServiceV2() throws ParseException {
        // Connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");

        // Initialize Spark session and read data from SQL table
        sparkSession = SparkSession.builder().master("local[*]").appName("InhouseTransferLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblinhousetransferline", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinhousetransferlinev3");
    }

    /**
     *
     * @param searchInhouseTransferLine
     * @return
     * @throws ParseException
     */
    public List<InhouseTransferLine> findInhouseTransferLinesV3(SearchInhouseTransferLine searchInhouseTransferLine) throws ParseException {
        Dataset<Row> inhouseTransferLineQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "TR_NO as transferNumber, "
                + "SRCE_ITM_CODE as sourceItemCode, "
                + "SRCE_STCK_TYP_ID as sourceStockTypeId, "
                + "SRCE_ST_BIN as sourceStorageBin, "
                + "TGT_ITM_CODE as targetItemCode, "
                + "TGT_STCK_TYP_ID as targetStockTypeId, "
                + "TGT_ST_BIN as targetStorageBin, "
                + "TR_ORD_QTY as transferOrderQty, "
                + "TR_CNF_QTY as transferConfirmedQty, "
                + "TR_UOM as transferUom, "
                + "PAL_CODE as palletCode, "
                + "CASE_CODE as caseCode, "
                + "PACK_BARCODE as packBarcodes, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
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
                + "REMARK as remarks, "
                + "IT_CTD_BY as createdBy, "
                + "IT_CTD_ON as createdOn, "
                + "IT_CNF_BY as confirmedBy, "
                + "IT_CNF_ON as confirmedOn, "
                + "IT_UTD_BY as updatedBy, "
                + "IT_UTD_ON as updatedOn "
                + "FROM tblinhousetransferlinev3 "
                + "WHERE IS_DELETED = 0 ");

        // Cache the DataFrame
        inhouseTransferLineQueryV3.cache();

        if (searchInhouseTransferLine.getLanguageId() != null && !searchInhouseTransferLine.getLanguageId().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("LANG_ID").isin(searchInhouseTransferLine.getLanguageId().toArray()));
        }
        if (searchInhouseTransferLine.getCompanyCodeId() != null && !searchInhouseTransferLine.getCompanyCodeId().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("C_ID").isin(searchInhouseTransferLine.getCompanyCodeId().toArray()));
        }
        if (searchInhouseTransferLine.getPlantId() != null && !searchInhouseTransferLine.getPlantId().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("PLANT_ID").isin(searchInhouseTransferLine.getPlantId().toArray()));
        }
        if (searchInhouseTransferLine.getWarehouseId() != null && !searchInhouseTransferLine.getWarehouseId().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("WH_ID").isin(searchInhouseTransferLine.getWarehouseId().toArray()));
        }
        if (searchInhouseTransferLine.getTransferNumber() != null && !searchInhouseTransferLine.getTransferNumber().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("TR_NO").isin(searchInhouseTransferLine.getTransferNumber().toArray()));
        }
        if (searchInhouseTransferLine.getSourceItemCode() != null && !searchInhouseTransferLine.getSourceItemCode().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("SRCE_ITM_CODE").isin(searchInhouseTransferLine.getSourceItemCode().toArray()));
        }
        if (searchInhouseTransferLine.getSourceStockTypeId() != null && !searchInhouseTransferLine.getSourceStockTypeId().isEmpty()) {
            List<String> sourceStockTypeIdStrings = searchInhouseTransferLine.getSourceStockTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("SRCE_STCK_TYP_ID").isin(sourceStockTypeIdStrings.toArray()));
        }
//        if (searchInhouseTransferLine.getStockTypeId() != null && !searchInhouseTransferLine.getStockTypeId().isEmpty()) {
//            List<String> stockTypeIdStrings = searchInhouseTransferLine.getStockTypeId().stream().map(String::valueOf).collect(Collectors.toList());
//            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("TGT_STCK_TYP_ID").isin(stockTypeIdStrings.toArray()));
//        }
        if (searchInhouseTransferLine.getSourceStorageBin() != null && !searchInhouseTransferLine.getSourceStorageBin().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("SRCE_ST_BIN").isin(searchInhouseTransferLine.getSourceStorageBin().toArray()));
        }
        if (searchInhouseTransferLine.getTargetItemCode() != null && !searchInhouseTransferLine.getTargetItemCode().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("TGT_ITM_CODE").isin(searchInhouseTransferLine.getTargetItemCode().toArray()));
        }
        if (searchInhouseTransferLine.getTargetStorageBin() != null && !searchInhouseTransferLine.getTargetStorageBin().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("TGT_ST_BIN").isin(searchInhouseTransferLine.getTargetStorageBin().toArray()));
        }
        if (searchInhouseTransferLine.getTransferConfirmedQty() != null && !searchInhouseTransferLine.getTransferConfirmedQty().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("TR_CNF_QTY").isin(searchInhouseTransferLine.getTransferConfirmedQty().toArray()));
        }
        if (searchInhouseTransferLine.getPackBarcodes() != null && !searchInhouseTransferLine.getPackBarcodes().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("PACK_BARCODE").isin(searchInhouseTransferLine.getPackBarcodes().toArray()));
        }
        if (searchInhouseTransferLine.getAvailableQty() != null && !searchInhouseTransferLine.getAvailableQty().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("AVAILABLE_QTY").isin(searchInhouseTransferLine.getAvailableQty().toArray()));
        }
        if (searchInhouseTransferLine.getStatusId() != null && !searchInhouseTransferLine.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchInhouseTransferLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }

        if (searchInhouseTransferLine.getRemarks() != null && !searchInhouseTransferLine.getRemarks().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("REMARK").isin(searchInhouseTransferLine.getRemarks().toArray()));
        }
        if (searchInhouseTransferLine.getCreatedBy() != null && !searchInhouseTransferLine.getCreatedBy().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CTD_BY").isin(searchInhouseTransferLine.getCreatedBy().toArray()));
        }
        if (searchInhouseTransferLine.getConfirmedBy() != null && !searchInhouseTransferLine.getConfirmedBy().isEmpty()) {
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CNF_BY").isin(searchInhouseTransferLine.getConfirmedBy().toArray()));
        }
        if (searchInhouseTransferLine.getStartCreatedOn() != null) {
            Date startDate = searchInhouseTransferLine.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInhouseTransferLine.getEndCreatedOn() != null) {
            Date endDate = searchInhouseTransferLine.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CTD_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (searchInhouseTransferLine.getStartConfirmedOn() != null) {
            Date startDate = searchInhouseTransferLine.getStartConfirmedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInhouseTransferLine.getEndConfirmedOn() != null) {
            Date endDate = searchInhouseTransferLine.getEndConfirmedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inhouseTransferLineQueryV3 = inhouseTransferLineQueryV3.filter(col("IT_CNF_ON").$less$eq(dateFormat.format(endDate)));
        }

        // Encode the result as InhouseTransferLine objects
        Encoder<InhouseTransferLine> inhouseTransferLineEncoder = Encoders.bean(InhouseTransferLine.class);
        Dataset<InhouseTransferLine> dataSet = inhouseTransferLineQueryV3.as(inhouseTransferLineEncoder);
        List<InhouseTransferLine> result = dataSet.collectAsList();

        return result;
    }
}
