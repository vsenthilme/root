package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.OutboundHeader;
import com.mnrclara.spark.core.model.wmscore.SearchOutboundHeader;
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
public class OutboundHeaderServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OutboundHeaderServiceV2() throws ParseException {
        // connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("OutBoundHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tbloutboundheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tbloutboundheaderv3");
    }

    /**
     *
     * @param searchOutboundHeader
     * @return
     * @throws ParseException
     */
    public List<OutboundHeader> findOutboundHeaderV3(SearchOutboundHeader searchOutboundHeader) throws ParseException {

        Dataset<Row> OutBoundHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_OB_NO as preOutboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "DLV_ORD_NO as deliveryOrderNo, "
                + "REF_DOC_TYP as referenceDocumentType, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "STATUS_ID as statusId, "
                + "REF_DOC_DATE as refDocDate, "
                + "REQ_DEL_DATE as requiredDeliveryDate, "
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
                + "DLV_CTD_BY as createdBy, "
                + "DLV_CTD_ON as createdOn, "
                + "DLV_CNF_BY as deliveryConfirmedBy, "
                + "DLV_CNF_ON as deliveryConfirmedOn, "
                + "DLV_UTD_BY as updatedBy, "
                + "DLV_UTD_ON as updatedOn, "
                + "DLV_REV_BY as reversedBy, "
                + "DLV_REV_ON as reversedOn "
                + "FROM tbloutboundheaderv3 WHERE IS_DELETED = 0 ");

        OutBoundHeaderQueryV3.cache();


        if (searchOutboundHeader.getWarehouseId() != null && !searchOutboundHeader.getWarehouseId().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("WH_ID").isin(searchOutboundHeader.getWarehouseId().toArray()));
        }
        if (searchOutboundHeader.getRefDocNumber() != null && !searchOutboundHeader.getRefDocNumber().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("REF_DOC_NO").isin(searchOutboundHeader.getRefDocNumber().toArray()));
        }
        if (searchOutboundHeader.getPartnerCode() != null && !searchOutboundHeader.getPartnerCode().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("PARTNER_CODE").isin(searchOutboundHeader.getPartnerCode().toArray()));
        }
        if (searchOutboundHeader.getSoType() != null && !searchOutboundHeader.getSoType().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("REF_FIELD_1").isin(searchOutboundHeader.getSoType().toArray()));
        }
        if (searchOutboundHeader.getStatusId() != null && !searchOutboundHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchOutboundHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (searchOutboundHeader.getOutboundOrderTypeId() != null && !searchOutboundHeader.getOutboundOrderTypeId().isEmpty()) {
            List<String> outboundOrderTypeIdStrings = searchOutboundHeader.getOutboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("OB_ORD_TYP_ID").isin(outboundOrderTypeIdStrings.toArray()));
        }
        if (searchOutboundHeader.getStartRequiredDeliveryDate() != null) {
            Date startDate = searchOutboundHeader.getStartRequiredDeliveryDate();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("REQ_DEL_DATE").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOutboundHeader.getEndRequiredDeliveryDate() != null) {
            Date endDate = searchOutboundHeader.getEndRequiredDeliveryDate();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("REQ_DEL_DATE").$less$eq(dateFormat.format(endDate)));
        }
        if (searchOutboundHeader.getStartDeliveryConfirmedOn() != null) {
            Date startDate = searchOutboundHeader.getStartDeliveryConfirmedOn();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("DLV_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOutboundHeader.getEndDeliveryConfirmedOn() != null) {
            Date endDate = searchOutboundHeader.getEndDeliveryConfirmedOn();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("DLV_CNF_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (searchOutboundHeader.getStartOrderDate() != null) {
            Date startDate = searchOutboundHeader.getStartOrderDate();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("DLV_CNF_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOutboundHeader.getEndOrderDate() != null) {
            Date endDate = searchOutboundHeader.getEndOrderDate();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("DLV_CNF_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (searchOutboundHeader.getLanguageId() != null && !searchOutboundHeader.getLanguageId().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("LANG_ID").isin(searchOutboundHeader.getLanguageId().toArray()));
        }
        if (searchOutboundHeader.getCompanyCodeId() != null && !searchOutboundHeader.getCompanyCodeId().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("C_ID").isin(searchOutboundHeader.getCompanyCodeId().toArray()));
        }
        if (searchOutboundHeader.getPlantId() != null && !searchOutboundHeader.getPlantId().isEmpty()) {
            OutBoundHeaderQueryV3 = OutBoundHeaderQueryV3.filter(col("PLANT_ID").isin(searchOutboundHeader.getPlantId().toArray()));
        }

        Encoder<OutboundHeader> outBoundHeaderEncoder = Encoders.bean(OutboundHeader.class);
        Dataset<OutboundHeader> dataSetControlGroup = OutBoundHeaderQueryV3.as(outBoundHeaderEncoder);
        List<OutboundHeader> result = dataSetControlGroup.collectAsList();

        return result;

    }
}
