package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.FindQualityHeader;
import com.mnrclara.spark.core.model.wmscore.QualityHeader;
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

@Service
@Slf4j
public class QualityHeaderServiceSpark {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public QualityHeaderServiceSpark() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("QualityHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblqualityheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblqualityheaderv3");
    }

    /**
     *
     * @param findQualityHeader
     * @return
     * @throws ParseException
     */
            public List<QualityHeader> findQualityHeader(FindQualityHeader findQualityHeader) throws ParseException {

        Dataset<Row> imQualityHeaderQuery = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_OB_NO as preOutboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "PU_NO as pickupNumber, "
                + "QC_NO as qualityInspectionNo, "
                + "PICK_HE_NO as actualHeNo, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "STATUS_ID as statusId, "
                + "QC_TO_QTY as qcToQty, "
                + "QC_UOM as qcUom, "
                + "MFR_PART as manufacturerPartNo, "
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
                + "QC_CTD_BY as qualityCreatedBy, "
                + "QC_CTD_ON as qualityCreatedOn, "
                + "QC_CNF_BY as qualityConfirmedBy, "
                + "QC_CNF_ON as qualityConfirmedOn, "
                + "QC_UTD_BY as qualityUpdatedBy, "
                + "QC_UTD_ON as qualityUpdatedOn, "
                + "QC_REV_BY as qualityReversedBy, "
                + "QC_REV_ON as qualityReversedOn "

                + "FROM tblqualityheaderv3 WHERE IS_DELETED = 0 ");

        if (findQualityHeader.getRefDocNumber() != null && !findQualityHeader.getRefDocNumber().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("REF_DOC_NO").isin(findQualityHeader.getRefDocNumber().toArray()));
        }
        if (findQualityHeader.getPartnerCode() != null && !findQualityHeader.getPartnerCode().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("PARTNER_CODE").isin(findQualityHeader.getPartnerCode().toArray()));
        }
        if (findQualityHeader.getQualityInspectionNo() != null && !findQualityHeader.getQualityInspectionNo().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("QC_NO").isin(findQualityHeader.getQualityInspectionNo().toArray()));
        }
        if (findQualityHeader.getActualHeNo() != null && !findQualityHeader.getActualHeNo().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("PICK_HE_NO").isin(findQualityHeader.getActualHeNo().toArray()));
        }
        if (findQualityHeader.getOutboundOrderTypeId() != null && !findQualityHeader.getOutboundOrderTypeId().isEmpty()) {
            List<String> outboundOrderString = findQualityHeader.getOutboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("OB_ORD_TYP_ID").isin(outboundOrderString.toArray()));
        }
        if (findQualityHeader.getStatusId() != null && !findQualityHeader.getStatusId().isEmpty()) {
            List<String> statusString = findQualityHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("STATUS_ID").isin(statusString.toArray()));
        }
        if (findQualityHeader.getSoType() != null && !findQualityHeader.getSoType().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("REF_FIELD_1").isin(findQualityHeader.getSoType().toArray()));
        }
        if (findQualityHeader.getWarehouseId() != null && !findQualityHeader.getWarehouseId().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("WH_ID").isin(findQualityHeader.getWarehouseId().toArray()));
        }
        if (findQualityHeader.getStartQualityCreatedOn() != null) {
            Date startQuality = findQualityHeader.getStartQualityCreatedOn();
            startQuality = org.apache.commons.lang3.time.DateUtils.truncate(startQuality, Calendar.DAY_OF_MONTH);
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("QC_CTD_ON").$greater$eq(dateFormat.format(startQuality)));
        }
        if (findQualityHeader.getEndQualityCreatedOn() != null) {
            Date endQualityDate = findQualityHeader.getEndQualityCreatedOn();
            endQualityDate = org.apache.commons.lang3.time.DateUtils.ceiling(endQualityDate, Calendar.DAY_OF_MONTH);
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("QC_CTD_ON").$less$eq(dateFormat.format(endQualityDate)));
        }

        // V2 fields
        if (findQualityHeader.getLanguageId() != null && !findQualityHeader.getLanguageId().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("LANG_ID").isin(findQualityHeader.getLanguageId().toArray()));
        }
        if (findQualityHeader.getCompanyCodeId() != null && !findQualityHeader.getCompanyCodeId().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("C_ID").isin(findQualityHeader.getCompanyCodeId().toArray()));
        }
        if (findQualityHeader.getPlantId() != null && !findQualityHeader.getPlantId().isEmpty()) {
            imQualityHeaderQuery = imQualityHeaderQuery.filter(col("PLANT_ID").isin(findQualityHeader.getPlantId().toArray()));
        }

        Encoder<QualityHeader> qualityHeaderEncoder = Encoders.bean(QualityHeader.class);
        Dataset<QualityHeader> dataSetControlGroup = imQualityHeaderQuery.as(qualityHeaderEncoder);
        List<QualityHeader> result = dataSetControlGroup.collectAsList();
        return result;
    }

}