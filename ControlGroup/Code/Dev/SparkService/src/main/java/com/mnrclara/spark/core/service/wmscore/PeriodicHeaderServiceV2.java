package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PeriodicHeader;
import com.mnrclara.spark.core.model.wmscore.SearchPeriodicHeader;
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
public class PeriodicHeaderServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PeriodicHeaderServiceV2() {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PeriodicHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblperiodicheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblperiodicheaderv3");
    }

    public List<PeriodicHeader> findPeriodicHeaderV3(SearchPeriodicHeader searchPeriodicHeader) throws ParseException {

        Dataset<Row> periodicHeaderQueryV3 = sparkSession.sql("Select "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "CC_TYP_ID as cycleCountTypeId, "
                + "CC_NO as cycleCountNo, "
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
                + "CC_CTD_BY as createdBy, "
                + "CC_CTD_ON as createdOn, "
                + "CC_CNT_BY as countedBy, "
                + "CC_CNT_ON as countedOn, "
                + "CC_CNF_BY as confirmedBy, "
                + "CC_CNF_ON as confirmedOn "
                + "From tblperiodicheaderv3 Where IS_DELETED = 0 "
        );
        periodicHeaderQueryV3.cache();

        if (searchPeriodicHeader.getWarehouseId() != null && !searchPeriodicHeader.getWarehouseId().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("WH_ID").isin(searchPeriodicHeader.getWarehouseId().toArray()));
        }
        if (searchPeriodicHeader.getCycleCountTypeId() != null && !searchPeriodicHeader.getCycleCountTypeId().isEmpty()) {
            List<String> cycleCountTypeIdStrings = searchPeriodicHeader.getCycleCountTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("CC_TYP_ID").isin(cycleCountTypeIdStrings.toArray()));
        }
        if (searchPeriodicHeader.getCycleCountNo() != null && !searchPeriodicHeader.getCycleCountNo().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("CC_NO").isin(searchPeriodicHeader.getCycleCountNo().toArray()));
        }
        if (searchPeriodicHeader.getHeaderStatusId() != null && !searchPeriodicHeader.getHeaderStatusId().isEmpty()) {
            List<String> headerStatusIdStrings = searchPeriodicHeader.getHeaderStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("STATUS_ID").isin(headerStatusIdStrings.toArray()));
        }
        if (searchPeriodicHeader.getCreatedBy() != null && !searchPeriodicHeader.getCreatedBy().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("CC_CTD_BY").isin(searchPeriodicHeader.getCreatedBy().toArray()));
        }
        if (searchPeriodicHeader.getStartCreatedOn() != null) {
            Date startDate = searchPeriodicHeader.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("CC_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchPeriodicHeader.getEndCreatedOn() != null) {
            Date endDate = searchPeriodicHeader.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("CC_CTD_ON").$greater$eq(dateFormat.format(endDate)));
        }
        if (searchPeriodicHeader.getCycleCounterId() != null && !searchPeriodicHeader.getCycleCounterId().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("COUNTER_ID").isin(searchPeriodicHeader.getCycleCounterId().toArray()));
        }
        if (searchPeriodicHeader.getLineStatusId() != null && !searchPeriodicHeader.getLineStatusId().isEmpty()) {
            List<String> LineStatusIdStrings = searchPeriodicHeader.getLineStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("STATUS_ID").isin(LineStatusIdStrings.toArray()));
        }
        if (searchPeriodicHeader.getLanguageId() != null && !searchPeriodicHeader.getLanguageId().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("LANG_ID").isin(searchPeriodicHeader.getLanguageId().toArray()));
        }
        if (searchPeriodicHeader.getCompanyCodeId() != null && !searchPeriodicHeader.getCompanyCodeId().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("C_ID").isin(searchPeriodicHeader.getCompanyCodeId().toArray()));
        }
        if (searchPeriodicHeader.getPlantId() != null && !searchPeriodicHeader.getPlantId().isEmpty()) {
            periodicHeaderQueryV3 = periodicHeaderQueryV3.filter(col("PLANT_ID").isin(searchPeriodicHeader.getPlantId().toArray()));
        }


        Encoder<PeriodicHeader> periodicHeaderEncoder = Encoders.bean(PeriodicHeader.class);
        Dataset<PeriodicHeader> datasetControlGroup = periodicHeaderQueryV3.as(periodicHeaderEncoder);
        List<PeriodicHeader> results = datasetControlGroup.collectAsList();
        return results;
    }
}
