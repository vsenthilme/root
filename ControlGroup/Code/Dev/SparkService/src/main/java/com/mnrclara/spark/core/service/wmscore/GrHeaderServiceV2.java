package com.mnrclara.spark.core.service.wmscore;

import com.mnrclara.spark.core.model.wmscore.GrHeader;

import com.mnrclara.spark.core.model.wmscore.SearchGrHeader;
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
public class GrHeaderServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GrHeaderServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("GrHeader.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblgrheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblgrheaderv3");

    }

    public List<GrHeader> findGrHeaderV3(SearchGrHeader searchGrHeader) throws ParseException{
        Dataset<Row> grHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_IB_NO as preInboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "STG_NO as stagingNo, "
                + "GR_NO as goodsReceiptNo, "
                + "PAL_CODE as palletCode, "
                + "CASE_CODE as caseCode, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "STATUS_ID as statusId, "
                + "GR_MTD as grMethod, "
                + "CONT_REC_NO as containerReceiptNo, "
                + "DOCK_ALL_NO as dockAllocationNo, "
                + "CONT_NO as containerNo, "
                + "VEH_NO as vechicleNo, "
                + "EA_DATE as expectedArrivalDate, "
                + "GR_DATE as goodsReceiptDate, "
                + "IS_DELETED as deletionIndicator, "
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
                + "GR_CTD_BY as createdBy, "
                + "GR_CTD_ON as createdOn, "
                + "GR_UTD_BY as updatedBy, "
                + "GR_UTD_ON as updatedOn, "
                + "GR_CNF_BY as confirmedBy, "
                + "GR_CNF_ON as confirmedOn "
                + "FROM tblgrheaderv3 WHERE IS_DELETED = 0");

        grHeaderQueryV3.cache();


        if (searchGrHeader.getInboundOrderTypeId() != null && !searchGrHeader.getInboundOrderTypeId().isEmpty()) {
            List<String> inboundOrderIdStrings = searchGrHeader.getInboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("IB_ORD_TYP_ID").isin(inboundOrderIdStrings.toArray()));
        }
        if (searchGrHeader.getGoodsReceiptNo() != null && !searchGrHeader.getGoodsReceiptNo().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("GR_NO").isin(searchGrHeader.getGoodsReceiptNo().toArray()));
        }
        if (searchGrHeader.getPreInboundNo() != null && !searchGrHeader.getPreInboundNo().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("PRE_IB_NO").isin(searchGrHeader.getPreInboundNo().toArray()));
        }
        if (searchGrHeader.getRefDocNumber() != null && !searchGrHeader.getRefDocNumber().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("REF_DOC_NO").isin(searchGrHeader.getRefDocNumber().toArray()));
        }
        if (searchGrHeader.getStatusId() != null && !searchGrHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchGrHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (searchGrHeader.getWarehouseId() != null && !searchGrHeader.getWarehouseId().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("WH_ID").isin(searchGrHeader.getWarehouseId().toArray()));
        }
        if (searchGrHeader.getCaseCode() != null && !searchGrHeader.getCaseCode().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("CASE_CODE").isin(searchGrHeader.getCaseCode().toArray()));
        }
        if (searchGrHeader.getCreatedBy() != null && !searchGrHeader.getCreatedBy().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("GR_CTD_BY").isin(searchGrHeader.getCreatedBy().toArray()));
        }
        if (searchGrHeader.getStartCreatedOn() != null) {
            Date startDate = searchGrHeader.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("GR_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchGrHeader.getEndCreatedOn() != null) {
            Date endDate = searchGrHeader.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("GR_CTD_ON").$less$eq(dateFormat.format(endDate)));
        }

        if (searchGrHeader.getCompanyCode() != null && !searchGrHeader.getCompanyCode().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("C_ID").isin(searchGrHeader.getCompanyCode().toArray()));
        }
        if (searchGrHeader.getPlantId() != null && !searchGrHeader.getPlantId().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("PLANT_ID").isin(searchGrHeader.getPlantId().toArray()));
        }
        if (searchGrHeader.getLanguageId() != null && !searchGrHeader.getLanguageId().isEmpty()) {
            grHeaderQueryV3 = grHeaderQueryV3.filter(col("LANG_ID").isin(searchGrHeader.getLanguageId().toArray()));
        }


        Encoder<GrHeader> GrHeaderEncoder = Encoders.bean(GrHeader.class);
        Dataset<GrHeader> dataSetControlGroup = grHeaderQueryV3.as(GrHeaderEncoder);
        List<GrHeader> result = dataSetControlGroup.collectAsList();
        grHeaderQueryV3.unpersist();

        return result;
    }
}

