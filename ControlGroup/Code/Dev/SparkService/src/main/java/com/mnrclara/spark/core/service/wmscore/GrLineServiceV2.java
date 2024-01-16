package com.mnrclara.spark.core.service.wmscore;



import com.mnrclara.spark.core.model.wmscore.GrLine;

import com.mnrclara.spark.core.model.wmscore.SearchGrLine;
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
public class GrLineServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GrLineServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("GrLine.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblGrline", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblGrlinev3");

    }

    public List<GrLine> findGrLineV3(SearchGrLine searchGrLine) throws ParseException {


        Dataset<Row>
                grLineQueryV3 = sparkSession.sql("SELECT " +
                " LANG_ID as languageId, " +
                " C_ID as companyCode, " +
                " PLANT_ID as plantId, " +
                " WH_ID as warehouseId, " +
                " PRE_IB_NO as preInboundNo, " +
                " REF_DOC_NO as refDocNumber, " +
                " GR_NO as goodsReceiptNo, " +
                " PAL_CODE as palletCode," +
                "CASE_CODE as caseCode, " +
                " PACK_BARCODE as packBarcodes, " +
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
                "ITEM_TEXT as itemDescription, " +
                "MFR_PART as manufacturerPartNo, " +
                "HSN_CODE as hsnCode, " +
                "VAR_TYP as variantType, " +
                "SPEC_ACTUAL as specificationActual, " +
                "ITM_BARCODE as itemBarcode, " +
                "ORD_QTY as orderQty, " +
                "ORD_UOM as orderUom, " +
                "GR_QTY as goodReceiptQty, " +
                "GR_UOM as grUom, " +
                "ACCEPT_QTY as acceptedQty, " +
                "DAMAGE_QTY as damageQty, " +
                "QTY_TYPE as quantityType, " +
                "ASS_USER_ID as assignedUserId, " +
                "PAWAY_HE_NO as putAwayHandlingEquipment, " +
                "PA_CNF_QTY as confirmedQty, " +
                "REM_QTY as remainingQty, " +
                "REF_ORD_NO as referenceOrderNo, " +
                "REF_ORD_QTY as referenceOrderQty, " +
                "CROSS_DOCK_ALLOC_QTY as crossDockAllocationQty, " +
                "MFR_DATE as manufacturerDate, " +
                "EXP_DATE as expiryDate, " +
                "STR_QTY as storageQty, " +
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
                "GR_CTD_BY as createdBy, " +
                "GR_CTD_ON as createdOn, " +
                "GR_UTD_BY as updatedBy, " +
                "GR_UTD_ON as updatedOn, " +
                "GR_CNF_BY as confirmedBy, " +
                "GR_CNF_ON as confirmedOn, " +
                "COMPANY_DESC as companyDescription, " +
                "PLANT_DESC as plantDescription, " +
                "WAREHOUSE_DESC as warehouseDescription " +
                "FROM tblGrlinev3 WHERE IS_DELETED = 0 ");

        grLineQueryV3.cache();


        if (searchGrLine.getPreInboundNo() != null && !searchGrLine.getPreInboundNo().isEmpty()) {
            grLineQueryV3 = grLineQueryV3.filter(col("PRE_IB_NO").isin(searchGrLine.getPreInboundNo().toArray()));
        }
        if (searchGrLine.getRefDocNumber() != null && !searchGrLine.getRefDocNumber().isEmpty()) {
            grLineQueryV3 = grLineQueryV3.filter(col("REF_DOC_NO").isin(searchGrLine.getRefDocNumber().toArray()));
        }

        if (searchGrLine.getCaseCode() != null && !searchGrLine.getCaseCode().isEmpty()) {
            grLineQueryV3 = grLineQueryV3.filter(col("CASE_CODE").isin(searchGrLine.getCaseCode().toArray()));
        }

        if (searchGrLine.getLineNo() != null && !searchGrLine.getLineNo().isEmpty()) {
            List<String> lineNoString = searchGrLine.getLineNo().stream().map(String::valueOf).collect(Collectors.toList());
            grLineQueryV3 = grLineQueryV3.filter(col("IB_LINE_NO").isin(lineNoString.toArray()));
        }
        if (searchGrLine.getItemCode() != null && !searchGrLine.getItemCode().isEmpty()) {
            grLineQueryV3 = grLineQueryV3.filter(col("ITM_CODE").isin(searchGrLine.getItemCode().toArray()));
        }
        if (searchGrLine.getStatusId() != null && !searchGrLine.getStatusId().isEmpty()) {
            List<String> statusString = searchGrLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            grLineQueryV3 = grLineQueryV3.filter(col("STATUS_ID").isin(statusString.toArray()));
        }
        if (searchGrLine.getPackBarcodes() != null && !searchGrLine.getPackBarcodes().isEmpty()) {
            grLineQueryV3 = grLineQueryV3.filter(col("PACK_BARCODE").isin(searchGrLine.getPackBarcodes().toArray()));
        }

        Encoder<GrLine> grLineEncoder = Encoders.bean(GrLine.class);
        Dataset<GrLine> dataSetControlGroup = grLineQueryV3.as(grLineEncoder);
        List<GrLine> result = dataSetControlGroup.collectAsList();

        return result;

    }
}
