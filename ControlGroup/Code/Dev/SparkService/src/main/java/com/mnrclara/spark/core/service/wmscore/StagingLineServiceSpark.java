package com.mnrclara.spark.core.service.wmscore;

import com.mnrclara.spark.core.model.wmscore.FindStagingLine;
import com.mnrclara.spark.core.model.wmscore.StagingLine;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Service
@Slf4j
public class StagingLineServiceSpark {


    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public StagingLineServiceSpark() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("StagingLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblstagingline", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblstagingline3");
    }

    public List<StagingLine> findStagingLine(FindStagingLine findStagingLine) throws ParseException {

        Dataset<Row>
                stagingLineQueryV2 =sparkSession.sql( "SELECT " +
                " LANG_ID as languageId, " +
                " C_ID as companyCode, " +
                " PLANT_ID as plantId, " +
                " WH_ID as warehouseId, " +
                " PRE_IB_NO as preInboundNo, " +
                " REF_DOC_NO as refDocNumber, " +
                "STG_NO as stagingNo, " +
                "PAL_CODE as palletCode," +
                "CASE_CODE as caseCode, " +
                "IB_LINE_NO as lineNo, " +
                "ITM_CODE as itemCode, " +
                "IB_ORD_TYP_ID as inboundOrderTypeId, " +
                "VAR_ID as variantCode, " +
                "VAR_SUB_ID as variantSubCode, " +
                "STR_NO as batchSerialNumber, " +
                "STCK_TYP_ID as stockTypeId, " +
                "SP_ST_IND_ID as specialStockIndicatorId, " +
                "ST_MTD as storageMethod, " +
                "STATUS_ID as statusId, " +
                "PARTNER_CODE as businessPartnerCode, " +
                "CONT_NO as containerNo, " +
                "INV_NO as invoiceNo, " +
                "ORD_QTY as orderQty, " +
                "ORD_UOM as orderUom, " +
                "ITM_PAL_QTY as itemQtyPerPallet, " +
                "ITM_CASE_QTY as itemQtyPerCase, " +
                "ASS_USER_ID as assignedUserId, " +
                "ITEM_TEXT as itemDescription, " +
                "MFR_PART as manufacturerPartNo, " +
                "HSN_CODE as hsnCode, " +
                "VAR_TYP as variantType, " +
                "SPEC_ACTUAL as specificationActual, " +
                "ITM_BARCODE as itemBarcode, " +
                "REF_ORD_NO as referenceOrderNo, " +
                "REF_ORD_QTY as referenceOrderQty, " +
                "CROSS_DOCK_ALLOC_QTY as crossDockAllocationQty, " +
                "REMARK as remarks, " +
                "REF_FIELD_1 as referenceField1, " +
                "REF_FIELD_2 as referenceField2, " +
                "REF_FIELD_3 as referenceField3, " +
                "REF_FIELD_4 as referenceField4, " +
                "REF_FIELD_5 as referenceField5, " +
                "REF_FIELD_6 as referenceField6, " +
                "REF_FIELD_7 as referenceField7, " +
                "REF_FIELD_8 as referenceField8, " +
                "REF_FIELD_9 as referenceField9, " +
                "REF_FIELD_10 as referenceField10, " +
                "IS_DELETED as deletionIndicator, " +
                "ST_CTD_BY as createdBy, " +
                "ST_CTD_ON as createdOn, " +
                "ST_UTD_BY as updatedBy, " +
                "ST_UTD_ON as updatedOn, " +
                "ST_CNF_BY as confirmedBy, " +
                "ST_CNF_ON as confirmedOn " +
//                "C_TEXT as companyDescription, " +
//                "PLANT_TEXT as plantDescription, " +
//                "WH_TEXT as warehouseDescription, " +
//                "STATUS_TEXT as statusDescription, " +
                "FROM tblstagingline3 WHERE IS_DELETED = 0 ");

        stagingLineQueryV2.cache();


        if (findStagingLine.getLanguageId() != null && !findStagingLine.getLanguageId().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("LANG_ID").isin(findStagingLine.getLanguageId().toArray()));
        }
        if (findStagingLine.getCompanyCode() != null && !findStagingLine.getCompanyCode().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("C_ID").isin(findStagingLine.getCompanyCode().toArray()));
        }
        if (findStagingLine.getPlantId() != null && !findStagingLine.getPlantId().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("PLANT_ID").isin(findStagingLine.getPlantId().toArray()));
        }
        if (findStagingLine.getWarehouseId() != null && !findStagingLine.getWarehouseId().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("WH_ID").isin(findStagingLine.getWarehouseId().toArray()));
        }

        if (findStagingLine.getPreInboundNo() != null && !findStagingLine.getPreInboundNo().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("PRE_IB_NO").isin(findStagingLine.getPreInboundNo().toArray()));
        }
        if (findStagingLine.getRefDocNumber() != null && !findStagingLine.getRefDocNumber().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("REF_DOC_NO").isin(findStagingLine.getRefDocNumber().toArray()));
        }
        if (findStagingLine.getStagingNo() != null && !findStagingLine.getStagingNo().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("STG_NO").isin(findStagingLine.getStagingNo().toArray()));
        }
        if (findStagingLine.getPalletCode() != null && !findStagingLine.getPalletCode().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("PAL_CODE").isin(findStagingLine.getPalletCode().toArray()));
        }
        if (findStagingLine.getCaseCode() != null && !findStagingLine.getCaseCode().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("CASE_CODE").isin(findStagingLine.getCaseCode().toArray()));
        }

        if (findStagingLine.getLineNo() != null && !findStagingLine.getLineNo().isEmpty()) {
            List<String> lineNoString = findStagingLine.getLineNo().stream().map(String::valueOf).collect(Collectors.toList());
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("IB_LINE_NO").isin(lineNoString.toArray()));
        }
        if (findStagingLine.getItemCode() != null && !findStagingLine.getItemCode().isEmpty()) {
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("ITM_CODE").isin(findStagingLine.getItemCode().toArray()));
        }
        if (findStagingLine.getStatusId() != null && !findStagingLine.getStatusId().isEmpty()) {
            List<String> statusString = findStagingLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            stagingLineQueryV2 = stagingLineQueryV2.filter(col("STATUS_ID").isin(statusString.toArray()));
        }




        Encoder<StagingLine> stagingLineEncoder = Encoders.bean(StagingLine.class);
        Dataset<StagingLine> dataSetControlGroup = stagingLineQueryV2.as(stagingLineEncoder);
        List<StagingLine> result = dataSetControlGroup.collectAsList();

        return result;
    }
}
