package com.mnrclara.spark.core.service.wmscore;



import com.mnrclara.spark.core.model.wmscore.InhouseTransferHeader;
import com.mnrclara.spark.core.model.wmscore.SearchInhouseTransferHeader;
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
public class InhouseTransferHeaderServiceV2 {
    Properties connProp = new Properties();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SparkSession sparkSession = null;

    public InhouseTransferHeaderServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("InhouseTransferHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblinhousetransferheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinhousetransferheaderV3");
    }

    /**
     * @return
     * @throws ParseException
     */
    public List<InhouseTransferHeader> findInhouseTransferHeaderV3(SearchInhouseTransferHeader searchInhouseTransferHeader) throws ParseException {

        Dataset<Row> inhouseTransferHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "TR_NO as transferNumber, "
                + "TR_TYP_ID as transferTypeId, "
                + "TR_MTD as transferMethod, "
                + "STATUS_ID as statusId, "
                + "COMPANY_DESC as companyDescription, "
                + "PLANT_DESC as plantDescription, "
                + "WAREHOUSE_DESC as warehouseDescription, "
                + "REMARK as remarks, "
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
                + "IT_CTD_BY as createdBy, "
                + "IT_CTD_ON as createdOn, "
                + "IT_UTD_BY as updatedBy, "
                + "IT_UTD_ON as updatedOn "
                + "From tblinhousetransferheaderV3 where IS_DELETED = 0 "
        );
        inhouseTransferHeaderQueryV3.cache();


        if (searchInhouseTransferHeader.getWarehouseId() != null && !searchInhouseTransferHeader.getWarehouseId().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("WH_ID").isin(searchInhouseTransferHeader.getWarehouseId().toArray()));
        }

        if (searchInhouseTransferHeader.getStatusId() != null && !searchInhouseTransferHeader.getStatusId().isEmpty()) {
            List<String> headerStatusIdStrings = searchInhouseTransferHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("STATUS_ID").isin(headerStatusIdStrings.toArray()));
        }

        if (searchInhouseTransferHeader.getStartCreatedOn() != null) {
            Date startDate = searchInhouseTransferHeader.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("IT_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInhouseTransferHeader.getEndCreatedOn() != null) {
            Date endDate = searchInhouseTransferHeader.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("IT_CTD_ON").$greater$eq(dateFormat.format(endDate)));
        }

        if (searchInhouseTransferHeader.getLanguageId() != null && !searchInhouseTransferHeader.getLanguageId().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("LANG_ID").isin(searchInhouseTransferHeader.getLanguageId().toArray()));
        }
        if (searchInhouseTransferHeader.getCompanyCodeId() != null && !searchInhouseTransferHeader.getCompanyCodeId().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("C_ID").isin(searchInhouseTransferHeader.getCompanyCodeId().toArray()));
        }
        if (searchInhouseTransferHeader.getPlantId() != null && !searchInhouseTransferHeader.getPlantId().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("PLANT_ID").isin(searchInhouseTransferHeader.getPlantId().toArray()));
        }
        if (searchInhouseTransferHeader.getTransferNumber() != null && !searchInhouseTransferHeader.getTransferNumber().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("TR_NO").isin(searchInhouseTransferHeader.getTransferNumber().toArray()));
        }
        if (searchInhouseTransferHeader.getCreatedBy() != null && !searchInhouseTransferHeader.getCreatedBy().isEmpty()) {
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("IT_CTD_BY").isin(searchInhouseTransferHeader.getCreatedBy().toArray()));
        }

        if (searchInhouseTransferHeader.getTransferTypeId() != null && !searchInhouseTransferHeader.getTransferTypeId().isEmpty()) {
            List<String> headerStatusIdStrings = searchInhouseTransferHeader.getTransferTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            inhouseTransferHeaderQueryV3 = inhouseTransferHeaderQueryV3.filter(col("TR_TYP_ID").isin(headerStatusIdStrings.toArray()));
        }

        Encoder<InhouseTransferHeader> inhouseTransferHeaderEncoder = Encoders.bean(InhouseTransferHeader.class);
        Dataset<InhouseTransferHeader> datasetControlGroup = inhouseTransferHeaderQueryV3.as(inhouseTransferHeaderEncoder);
        List<InhouseTransferHeader> results = datasetControlGroup.collectAsList();
        return results;
    }
}
