package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.OutboundReversal;
import com.mnrclara.spark.core.model.wmscore.SearchOutboundReversal;
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
public class OutboundReversalServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OutboundReversalServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("OutboundReversal.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tbloutboundreversal", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tbloutboundreversalv3");
    }

    /**
     *
     * @param searchOutboundReversal
     * @return
     * @throws ParseException
     */
    public List<OutboundReversal> findOutboundReversalV3(SearchOutboundReversal searchOutboundReversal) throws ParseException {


        Dataset<Row> outboundReversalrQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "OB_REVERSAL_NO as outboundReversalNo, "
                + "REVERSAL_TYPE as reversalType, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "ITM_CODE as itemCode,"
                + "PACK_BARCODE as packBarcode," +
                " REV_QTY as reversedQty," +
                "STATUS_ID as statusId," +
                "REF_FIELD_1 as referenceField1," +
                "REF_FIELD_2 as referenceField2," +
                "REF_FIELD_3 as referenceField3," +
                "REF_FIELD_4 as referenceField4," +
                "REF_FIELD_5 as referenceField5," +
                "REF_FIELD_6 as referenceField6," +
                "REF_FIELD_7 as referenceField7," +
                "REF_FIELD_8 as referenceField8," +
                "REF_FIELD_9 as referenceField9," +
                "REF_FIELD_10 as referenceField10," +
                "IS_DELETED as deletionIndicator," +
                "OB_REV_BY as reversedBy," +
                "OB_REV_ON as reversedOn " +
                " FROM tbloutboundreversalv3 WHERE IS_DELETED = 0 ");

        outboundReversalrQueryV3.cache();

        if (searchOutboundReversal.getOutboundReversalNo() != null && !searchOutboundReversal.getOutboundReversalNo().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("OB_REVERSAL_NO").isin(searchOutboundReversal.getOutboundReversalNo().toArray()));
        }
        if (searchOutboundReversal.getReversalType() != null && !searchOutboundReversal.getReversalType().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("REVERSAL_TYPE").isin(searchOutboundReversal.getReversalType().toArray()));
        }
        if (searchOutboundReversal.getRefDocNumber() != null && !searchOutboundReversal.getRefDocNumber().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("REF_DOC_NO").isin(searchOutboundReversal.getRefDocNumber().toArray()));
        }
        if (searchOutboundReversal.getPartnerCode() != null && !searchOutboundReversal.getPartnerCode().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("PARTNER_CODE").isin(searchOutboundReversal.getPartnerCode().toArray()));
        }
        if (searchOutboundReversal.getItemCode() != null && !searchOutboundReversal.getItemCode().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("ITM_CODE").isin(searchOutboundReversal.getItemCode().toArray()));
        }
        if (searchOutboundReversal.getPackBarcode() != null && !searchOutboundReversal.getPackBarcode().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("PACK_BARCODE").isin(searchOutboundReversal.getPackBarcode().toArray()));
        }
        if (searchOutboundReversal.getReversedBy() != null && !searchOutboundReversal.getReversedBy().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("OB_REV_BY").isin(searchOutboundReversal.getReversedBy().toArray()));
        }
        if (searchOutboundReversal.getStatusId() != null && !searchOutboundReversal.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchOutboundReversal.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (searchOutboundReversal.getStartReversedOn() != null) {
            Date startDate = searchOutboundReversal.getStartReversedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("OB_REV_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOutboundReversal.getEndReversedOn() != null) {
            Date endDate = searchOutboundReversal.getEndReversedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("OB_REV_ON").$less$eq(dateFormat.format(endDate)));
        }

        if (searchOutboundReversal.getLanguageId() != null && !searchOutboundReversal.getLanguageId().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("LANG_ID").isin(searchOutboundReversal.getLanguageId().toArray()));
        }
        if (searchOutboundReversal.getCompanyCodeId() != null && !searchOutboundReversal.getCompanyCodeId().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("C_ID").isin(searchOutboundReversal.getCompanyCodeId().toArray()));
        }
        if (searchOutboundReversal.getPlantId() != null && !searchOutboundReversal.getPlantId().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("PLANT_ID").isin(searchOutboundReversal.getPlantId().toArray()));
        }

        if (searchOutboundReversal.getWarehouseId() != null && !searchOutboundReversal.getWarehouseId().isEmpty()) {
            outboundReversalrQueryV3 = outboundReversalrQueryV3.filter(col("PLANT_ID").isin(searchOutboundReversal.getWarehouseId().toArray()));
        }

        Encoder<OutboundReversal> outboundReversalEncoder = Encoders.bean(OutboundReversal.class);
        Dataset<OutboundReversal> dataSetControlGroup = outboundReversalrQueryV3.as(outboundReversalEncoder);
        List<OutboundReversal> result = dataSetControlGroup.collectAsList();

        return result;
    }
}
