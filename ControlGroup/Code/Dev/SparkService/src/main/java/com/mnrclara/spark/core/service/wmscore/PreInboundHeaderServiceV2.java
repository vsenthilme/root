package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PreInboundHeader;
import com.mnrclara.spark.core.model.wmscore.SearchPreInboundHeader;
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
public class PreInboundHeaderServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PreInboundHeaderServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PreInboundHeader.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblpreinboundheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblpreinboundheaderv3");

    }

    public List<PreInboundHeader> findPreInboundHeaderV3(SearchPreInboundHeader searchPreInboundHeader) throws ParseException {
        Dataset<Row> preInboundHeaderQueryv3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_IB_NO as preInboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "REF_DOC_TYP as referenceDocumentType, "
                + "STATUS_ID as statusId, "
                + "CONT_NO as containerNo, "
                + "NO_CONTAINERS as noOfContainers, "
                + "CONT_TYP as containerType, "
                + "REF_DOC_DATE as refDocDate, "
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
                + "UTD_ON as updatedOn "
                + "FROM tblpreinboundheaderv3 WHERE IS_DELETED = 0");
        preInboundHeaderQueryv3.cache();


        if (searchPreInboundHeader.getLanguageId() != null && !searchPreInboundHeader.getLanguageId().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("LANG_ID").isin(searchPreInboundHeader.getLanguageId().toArray()));
        }
        if (searchPreInboundHeader.getCompanyCode() != null && !searchPreInboundHeader.getCompanyCode().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("C_ID").isin(searchPreInboundHeader.getCompanyCode().toArray()));
        }
        if (searchPreInboundHeader.getPlantId() != null && !searchPreInboundHeader.getPlantId().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("PLANT_ID").isin(searchPreInboundHeader.getPlantId().toArray()));
        }
        if (searchPreInboundHeader.getWarehouseId() != null && !searchPreInboundHeader.getWarehouseId().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("WH_ID").isin(searchPreInboundHeader.getWarehouseId().toArray()));
        }
        if (searchPreInboundHeader.getPreInboundNo() != null && !searchPreInboundHeader.getPreInboundNo().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("PRE_IB_NO").isin(searchPreInboundHeader.getPreInboundNo().toArray()));
        }
        if (searchPreInboundHeader.getInboundOrderTypeId() != null && !searchPreInboundHeader.getInboundOrderTypeId().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("IB_ORD_TYP_ID").isin(searchPreInboundHeader.getInboundOrderTypeId().toArray()));
        }
        if (searchPreInboundHeader.getRefDocNumber() != null && !searchPreInboundHeader.getRefDocNumber().isEmpty()) {
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("REF_DOC_NO").isin(searchPreInboundHeader.getRefDocNumber().toArray()));
        }
        if (searchPreInboundHeader.getStatusId() != null && !searchPreInboundHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchPreInboundHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (searchPreInboundHeader.getStartCreatedOn() != null) {
            Date startDate = searchPreInboundHeader.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchPreInboundHeader.getEndCreatedOn() != null) {
            Date endDate = searchPreInboundHeader.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("CTD_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (searchPreInboundHeader.getStartRefDocDate() != null) {
            Date startDate = searchPreInboundHeader.getStartRefDocDate();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchPreInboundHeader.getEndRefDocDate() != null) {
            Date endDate = searchPreInboundHeader.getEndRefDocDate();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            preInboundHeaderQueryv3 = preInboundHeaderQueryv3.filter(col("CTD_ON").$less$eq(dateFormat.format(endDate)));
        }

        Encoder<PreInboundHeader> PreInboundHeaderEncoder = Encoders.bean(PreInboundHeader.class);
        Dataset<PreInboundHeader> dataSetControlGroup = preInboundHeaderQueryv3.as(PreInboundHeaderEncoder);
        List<PreInboundHeader> result = dataSetControlGroup.collectAsList();

        return result;
    }
}
