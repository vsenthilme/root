package com.mnrclara.spark.core.service.wmscore;






import com.mnrclara.spark.core.model.wmscore.InboundHeader;
import com.mnrclara.spark.core.model.wmscore.SearchInboundHeader;
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
public class InboundHeaderServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InboundHeaderServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("InboundHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblinboundheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblinboundheaderv3");

    }

    /**
     *
     * @return
     * @throws ParseException
     */
    public List<InboundHeader> findInboundHeaderV3(SearchInboundHeader searchInboundHeader) throws ParseException {


        Dataset<Row> inboundHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCode, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "REF_DOC_NO as refDocNumber, "
                + "PRE_IB_NO as preInboundNo, "
                + "STATUS_ID as statusId, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "CONT_NO as containerNo, "
                + "VEH_NO as vechicleNo, "
                + "IB_TEXT as headerText, "
                + "IS_DELETED as deletionIndicator, "
                + "COMPANY_DESC as companyDescription, "
                + "PLANT_DESC as plantDescription, "
                + "WAREHOUSE_DESC as warehouseDescription, "
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
                + "CTD_BY as createdBy, "
                + "CTD_ON as createdOn, "
                + "UTD_BY as updatedBy, "
                + "UTD_ON as updatedOn, "
                + "IB_CNF_BY as confirmedBy, "
                + "IB_CNF_ON as confirmedOn "
                + "FROM tblinboundheaderv3 WHERE IS_DELETED =0 ");

        inboundHeaderQueryV3.cache();

        if (searchInboundHeader.getWarehouseId() != null && !searchInboundHeader.getWarehouseId().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("WH_ID").isin(searchInboundHeader.getWarehouseId().toArray()));
        }
        if (searchInboundHeader.getContainerNo() != null && !searchInboundHeader.getContainerNo().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("CONT_NO").isin(searchInboundHeader.getContainerNo().toArray()));
        }
        if (searchInboundHeader.getStatusId() != null && !searchInboundHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchInboundHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (searchInboundHeader.getInboundOrderTypeId() != null && !searchInboundHeader.getInboundOrderTypeId().isEmpty()) {
            List<String> inboundIdStrings = searchInboundHeader.getInboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("IB_ORD_TYP_ID").isin(inboundIdStrings.toArray()));
        }
        if (searchInboundHeader.getRefDocNumber() != null && !searchInboundHeader.getRefDocNumber().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("REF_DOC_NO").isin(searchInboundHeader.getRefDocNumber().toArray()));
        }
        //v2 fields
        if (searchInboundHeader.getCompanyCode() != null && !searchInboundHeader.getCompanyCode().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("C_ID").isin(searchInboundHeader.getCompanyCode().toArray()));
        }
        if (searchInboundHeader.getPlantId() != null && !searchInboundHeader.getPlantId().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("PLANT_ID").isin(searchInboundHeader.getPlantId().toArray()));
        }
        if (searchInboundHeader.getLanguageId() != null && !searchInboundHeader.getLanguageId().isEmpty()) {
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("LANG_ID").isin(searchInboundHeader.getLanguageId().toArray()));
        }

        if (searchInboundHeader.getStartCreatedOn() != null) {
            Date startDate = searchInboundHeader.getStartCreatedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInboundHeader.getEndCreatedOn() != null) {
            Date endDate = searchInboundHeader.getEndCreatedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("CTD_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (searchInboundHeader.getStartConfirmedOn() != null) {
            Date startDate = searchInboundHeader.getStartConfirmedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("IB_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchInboundHeader.getEndConfirmedOn() != null) {
            Date endDate = searchInboundHeader.getEndConfirmedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            inboundHeaderQueryV3 = inboundHeaderQueryV3.filter(col("IB_CNF_ON").$less$eq(dateFormat.format(endDate)));
        }


        Encoder<InboundHeader> inboundHeaderEncoder = Encoders.bean(InboundHeader.class);
        Dataset<InboundHeader> dataSetControlGroup = inboundHeaderQueryV3.as(inboundHeaderEncoder);
        List<InboundHeader> result = dataSetControlGroup.collectAsList();
        return result;
    }
}
