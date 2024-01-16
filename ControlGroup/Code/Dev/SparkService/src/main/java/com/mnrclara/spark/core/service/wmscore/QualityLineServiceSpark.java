package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.FindQualityLine;
import com.mnrclara.spark.core.model.wmscore.QualityLine;
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
public class QualityLineServiceSpark {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");

    public QualityLineServiceSpark() throws ParseException {
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("QualityLineExample.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblqualityline", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblqualitylinev3");


    }

    /**
     *
     * @param findQualityLine
     * @return
     * @throws ParseException
     */
    public List<QualityLine> findQualityLine(FindQualityLine findQualityLine) throws ParseException {
        Dataset<Row>
                qualityLineQueryV2 = sparkSession.sql("SELECT " +
                "LANG_ID as languageId," +
                "C_ID as companyCodeId," +
                "PLANT_ID as plantId," +
                "WH_ID as warehouseId," +
                "PRE_OB_NO as preOutboundNo," +
                "REF_DOC_NO as refDocNumber," +
                "PARTNER_CODE as partnerCode," +
                "OB_LINE_NO as lineNumber ," +
                "QC_NO as qualityInspectionNo," +
                "ITM_CODE as itemCode ," +
                "PICK_HE_NO as actualHeNo ," +
                "PICK_PACK_BARCODE as pickPackBarCode ," +
                "OB_ORD_TYP_ID as outboundOrderTypeId ," +
                "STATUS_ID as statusId ," +
                "STCK_TYP_ID as stockTypeId ," +
                "SP_ST_IND_ID as specialStockIndicatorId ," +
                "ITEM_TEXT as description ," +
                "MFR_PART as manufacturerPartNo ," +
                "PACK_MT_NO as packingMaterialNo ," +
                "VAR_ID as variantCode," +
                "VAR_SUB_ID as variantSubCode ," +
                "STR_NO as batchSerialNumber ," +
                "QC_QTY as qualityQty ," +
                "PICK_CNF_QTY as pickConfirmQty ," +
                "QC_UOM as qualityConfirmUom ," +
                "REJ_QTY as rejectQty ," +
                "REJ_UOM as rejectUom ," +
                "REF_FIELD_1 as referenceField1 ," +
                "REF_FIELD_2 as referenceField2 ," +
                "REF_FIELD_3 as referenceField3 ," +
                "REF_FIELD_4 as referenceField4 ," +
                "REF_FIELD_5 as referenceField5 ," +
                "REF_FIELD_6 as referenceField6 ," +
                "REF_FIELD_7 as referenceField7 ," +
                "REF_FIELD_8 as referenceField8 ," +
                "REF_FIELD_9 as referenceField9 ," +
                "REF_FIELD_10 as referenceField10 ," +
                "IS_DELETED as deletionIndicator ," +
                "QC_CTD_BY as qualityCreatedBy ," +
                "QC_CTD_ON as qualityCreatedOn ," +
                "QC_CNF_BY as qualityConfirmedBy ," +
                "QC_CNF_ON as qualityConfirmedOn ," +
                "QC_UTD_BY as qualityUpdatedBy ," +
                "QC_UTD_ON as qualityUpdatedOn ," +
                "QC_REV_BY as qualityReversedBy ," +
                "QC_REV_ON as qualityReversedOn ," +
                "COMPANY_DESC as companyDescription ," +
                "PLANT_DESC as plantDescription ," +
                "WAREHOUSE_DESC as warehouseDescription ," +
                "STATUS_DESC as statusDescription   " +
                "FROM tblqualitylinev3 WHERE IS_DELETED = 0 ");

        qualityLineQueryV2.cache();

        if (findQualityLine.getLanguageId() != null && !findQualityLine.getLanguageId().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("LANG_ID").isin(findQualityLine.getLanguageId().toArray()));
        }
        if (findQualityLine.getCompanyCodeId() != null && !findQualityLine.getCompanyCodeId().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("C_ID").isin(findQualityLine.getCompanyCodeId().toArray()));
        }
        if (findQualityLine.getPlantId() != null && !findQualityLine.getPlantId().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("PLANT_ID").isin(findQualityLine.getPlantId().toArray()));
        }
        if (findQualityLine.getWarehouseId() != null && !findQualityLine.getWarehouseId().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("WH_ID").isin(findQualityLine.getWarehouseId().toArray()));
        }
        if (findQualityLine.getRefDocNumber() != null && !findQualityLine.getRefDocNumber().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("REF_DOC_NO").isin(findQualityLine.getRefDocNumber().toArray()));
        }
        if (findQualityLine.getPartnerCode() != null && !findQualityLine.getPartnerCode().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("PARTNER_CODE").isin(findQualityLine.getPartnerCode().toArray()));
        }
        if (findQualityLine.getQualityInspectionNo() != null && !findQualityLine.getQualityInspectionNo().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("QC_NO").isin(findQualityLine.getQualityInspectionNo().toArray()));
        }
        if (findQualityLine.getStatusId() != null && !findQualityLine.getStatusId().isEmpty()) {
            List<String> statusString = findQualityLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("STATUS_ID").isin(statusString.toArray()));
        }
        if (findQualityLine.getPreOutboundNo() != null && !findQualityLine.getPreOutboundNo().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("PRE_OB_NO").isin(findQualityLine.getPreOutboundNo().toArray()));
        }
        if (findQualityLine.getLineNumber() != null && !findQualityLine.getLineNumber().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("OB_LINE_NO").isin(findQualityLine.getLineNumber().toArray()));
        }
        if (findQualityLine.getItemCode() != null && !findQualityLine.getItemCode().isEmpty()) {
            qualityLineQueryV2 = qualityLineQueryV2.filter(col("ITM_CODE").isin(findQualityLine.getItemCode().toArray()));
        }


        Encoder<QualityLine> QualityLineEncoder = Encoders.bean(QualityLine.class);
        Dataset<QualityLine> dataSetControlGroup = qualityLineQueryV2.as(QualityLineEncoder);
        List<QualityLine> result = dataSetControlGroup.collectAsList();

        return result;
    }
}