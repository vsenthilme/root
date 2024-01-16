package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.FindPutAwayLine;
import com.mnrclara.spark.core.model.wmscore.PutAwayLine;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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

@Slf4j
@Service
public class PutAwayLineServiceSpark {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PutAwayLineServiceSpark() {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PutAwayLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblputawayline", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblputawaylinev3");
    }

    public List<PutAwayLine> findPutAwayLine(FindPutAwayLine findPutAwayLine) throws ParseException {

        Dataset<Row> putAwayLineV2Query = sparkSession.sql("Select "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "GR_NO as goodsReceiptNo, "
                + "PRE_IB_NO as preInboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PA_NO as putAwayNumber, "
                + "IB_LINE_NO as lineNo, "
                + "ITM_CODE as itemCode, "
                + "PROP_ST_BIN as proposedStorageBin, "
                + "CNF_ST_BIN as confirmedStorageBin, "
                + "PACK_BARCODE as packBarcodes, "
                + "PA_QTY as putAwayQuantity, "
                + "PA_UOM as putAwayUom, "
                + "PA_CNF_QTY as putawayConfirmedQty, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "ST_MTD as storageMethod, "
                + "STR_NO as batchSerialNumber, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "REF_ORD_NO as referenceOrderNo, "
                + "STATUS_ID as statusId, "
                + "TEXT as description, "
                + "SPEC_ACTUAL as specificationActual, "
                + "VEN_CODE as vendorCode, "
                + "MFR_PART as manufacturerPartNo, "
                + "HSN_CODE as hsnCode, "
                + "ITM_BARCODE as itemBarcode, "
                + "MFR_DATE as manufacturerDate, "
                + "EXP_DATE as expiryDate, "
                + "STR_QTY as storageQty, "
                + "ST_TEMP as storageTemperature, "
                + "ST_UOM as storageUom, "
                + "QTY_TYPE as quantityType, "
                + "PROP_HE_NO as proposedHandlingEquipment, "
                + "ASS_USER_ID as assignedUserId, "
                + "WRK_CTR_ID as workCenterId, "
                + "PAWAY_HE_NO as putAwayHandlingEquipment, "
                + "PAWAY_EMP_ID as putAwayEmployeeId, "
                + "CREATE_REMARK as createRemarks, "
                + "CNF_REMARK as cnfRemarks, "
                + "IS_DELETED as deletionIndicator,"
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
                + "PA_CTD_BY as createdBy, "
                + "PA_CTD_ON as createdOn, "
                + "PA_CNF_BY as confirmedBy, "
                + "PA_CNF_ON as confirmedOn, "
                + "PA_UTD_BY as updatedBy, "
                + "PA_UTD_ON as updatedOn, "
                + "COMPANY_DESC as companyDescription, "
                + "PLANT_DESC as plantDescription, "
                + "WAREHOUSE_DESC as warehouseDescription, "
                + "STATUS_DESC as statusDescription "
                + "From tblputawaylinev3 Where IS_DELETED = 0 "
        );
        putAwayLineV2Query.cache();

        if (findPutAwayLine.getWarehouseId() != null && !findPutAwayLine.getWarehouseId().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("WH_ID").isin(findPutAwayLine.getWarehouseId().toArray()));
        }
        if (findPutAwayLine.getGoodsReceiptNo() != null && !findPutAwayLine.getGoodsReceiptNo().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("GR_NO").isin(findPutAwayLine.getGoodsReceiptNo().toArray()));
        }
        if (findPutAwayLine.getPreInboundNo() != null && !findPutAwayLine.getPreInboundNo().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PRE_IB_NO").isin(findPutAwayLine.getPreInboundNo().toArray()));
        }
        if (findPutAwayLine.getRefDocNumber() != null && !findPutAwayLine.getRefDocNumber().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("REF_DOC_NO").isin(findPutAwayLine.getRefDocNumber().toArray()));
        }
        if (findPutAwayLine.getPutAwayNumber() != null && !findPutAwayLine.getPutAwayNumber().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PA_NO").isin(findPutAwayLine.getPutAwayNumber().toArray()));
        }
        if (findPutAwayLine.getLineNo() != null && !findPutAwayLine.getLineNo().isEmpty()) {
            List<String> lineToString = findPutAwayLine.getLineNo().stream().map(String::valueOf).collect(Collectors.toList());
            putAwayLineV2Query = putAwayLineV2Query.filter(col("IB_LINE_NO").isin(lineToString.toArray()));
        }
        if (findPutAwayLine.getItemCode() != null && !findPutAwayLine.getItemCode().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("ITM_CODE").isin(findPutAwayLine.getItemCode().toArray()));
        }
        if (findPutAwayLine.getProposedStorageBin() != null && !findPutAwayLine.getProposedStorageBin().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PROP_ST_BIN").isin(findPutAwayLine.getProposedStorageBin().toArray()));
        }
        if (findPutAwayLine.getConfirmedStorageBin() != null && !findPutAwayLine.getConfirmedStorageBin().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("CNF_ST_BIN").isin(findPutAwayLine.getConfirmedStorageBin().toArray()));
        }
        if (findPutAwayLine.getPackBarCodes() != null && !findPutAwayLine.getPackBarCodes().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PACK_BARCODE").isin(findPutAwayLine.getPackBarCodes().toArray()));
        }
        if (findPutAwayLine.getFromConfirmedDate() != null) {
            Date startDate = findPutAwayLine.getFromConfirmedDate();
            startDate = org.apache.commons.lang3.time.DateUtils.ceiling(startDate, Calendar.DAY_OF_MONTH);
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PA_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (findPutAwayLine.getToConfirmedDate() != null) {
            Date endDate = findPutAwayLine.getToConfirmedDate();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PA_CNF_ON").$greater$eq(dateFormat.format(endDate)));
        }
        if (findPutAwayLine.getFromCreatedDate() != null) {
            Date startDate = findPutAwayLine.getFromCreatedDate();
            startDate = org.apache.commons.lang3.time.DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PA_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (findPutAwayLine.getToCreatedDate() != null) {
            Date endDate = findPutAwayLine.getToCreatedDate();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PA_CTD_ON").$greater$eq(dateFormat.format(endDate)));
        }

        // V2 fields
        if (findPutAwayLine.getLanguageId() != null && !findPutAwayLine.getLanguageId().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("LANG_ID").isin(findPutAwayLine.getLanguageId().toArray()));
        }
        if (findPutAwayLine.getCompanyCode() != null && !findPutAwayLine.getCompanyCode().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("C_ID").isin(findPutAwayLine.getCompanyCode().toArray()));
        }
        if (findPutAwayLine.getPlantId() != null && !findPutAwayLine.getPlantId().isEmpty()) {
            putAwayLineV2Query = putAwayLineV2Query.filter(col("PLANT_ID").isin(findPutAwayLine.getPlantId().toArray()));
        }

        Encoder<PutAwayLine> putAwayLineEncoder = Encoders.bean(PutAwayLine.class);
        Dataset<PutAwayLine> datasetControlGroup = putAwayLineV2Query.as(putAwayLineEncoder);
        List<PutAwayLine> results = datasetControlGroup.collectAsList();
        return results;
    }
}
