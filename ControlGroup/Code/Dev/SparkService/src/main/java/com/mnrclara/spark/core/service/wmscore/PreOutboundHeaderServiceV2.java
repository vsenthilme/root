package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PreOutboundHeader;
import com.mnrclara.spark.core.model.wmscore.SearchPreOutboundHeader;
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

@Slf4j
@Service
public class PreOutboundHeaderServiceV2 {
    Properties conProp = new Properties();

    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PreOutboundHeaderServiceV2() throws ParseException {
        //connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PreOutboundHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblpreoutboundheader", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblpreoutboundheaderv3");
    }

    /**
     *
     * @param searchPreOutboundHeader
     * @return
     * @throws ParseException
     */
    public List<PreOutboundHeader> findPreOutboundHeaderV3(SearchPreOutboundHeader searchPreOutboundHeader) throws ParseException {

        Dataset<Row> preObHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "REF_DOC_NO as refDocNumber, "
                + "PRE_OB_NO as preOutboundNo, "
                + "PARTNER_CODE as partnerCode, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "REF_DOC_TYP as referenceDocumentType, "
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
                + "PRE_OB_CTD_BY as createdBy, "
                + "PRE_OB_CTD_ON as createdOn, "
                + "PRE_OB_UTD_BY as updatedBy, "
                + "PRE_OB_UTD_ON as updatedOn "
                + "FROM tblpreoutboundheaderv3 WHERE IS_DELETED=0 ");

        preObHeaderQueryV3.cache();


        if (searchPreOutboundHeader.getWarehouseId() != null && !searchPreOutboundHeader.getWarehouseId().isEmpty()) {
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("WH_ID").isin(searchPreOutboundHeader.getWarehouseId().toArray()));
        }
        if (searchPreOutboundHeader.getPreOutboundNo() != null && !searchPreOutboundHeader.getPreOutboundNo().isEmpty()) {
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PRE_OB_NO").isin(searchPreOutboundHeader.getPreOutboundNo().toArray()));
        }
        if (searchPreOutboundHeader.getOutboundOrderTypeId() != null && !searchPreOutboundHeader.getOutboundOrderTypeId().isEmpty()) {
            List<String> outboundOrderTypeIdStrings = searchPreOutboundHeader.getOutboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("OB_ORD_TYP_ID").isin(outboundOrderTypeIdStrings.toArray()));
        }
        if(searchPreOutboundHeader.getSoType() != null && !searchPreOutboundHeader.getSoType().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REF_FIELD_1").isin(searchPreOutboundHeader.getSoType().toArray()));
        }
        if(searchPreOutboundHeader.getSoNumber() != null && !searchPreOutboundHeader.getSoNumber().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REF_DOC_NO").isin(searchPreOutboundHeader.getSoNumber().toArray()));
        }
        if(searchPreOutboundHeader.getPartnerCode() != null && !searchPreOutboundHeader.getPartnerCode().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PARTNER_CODE").isin(searchPreOutboundHeader.getPartnerCode().toArray()));
        }
        if (searchPreOutboundHeader.getStatusId() != null && !searchPreOutboundHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchPreOutboundHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if(searchPreOutboundHeader.getCreatedBy() != null && !searchPreOutboundHeader.getCreatedBy().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PRE_OB_CTD_BY").isin(searchPreOutboundHeader.getCreatedBy().toArray()));
        }
        if (searchPreOutboundHeader.getStartRequiredDeliveryDate() != null) {
            Date startRequireDeliveryDate = searchPreOutboundHeader.getStartRequiredDeliveryDate();
            startRequireDeliveryDate = DateUtils.truncate(startRequireDeliveryDate, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REQ_DEL_DATE").$greater$eq(dateFormat.format(startRequireDeliveryDate)));
        }
        if (searchPreOutboundHeader.getEndRequiredDeliveryDate() != null) {
            Date endRequireDeliveryDate = searchPreOutboundHeader.getEndRequiredDeliveryDate();
            endRequireDeliveryDate = DateUtils.ceiling(endRequireDeliveryDate, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REQ_DEL_DATE").$less$eq(dateFormat.format(endRequireDeliveryDate)));
        }
        if (searchPreOutboundHeader.getStartOrderDate() != null) {
            Date startOrderDate = searchPreOutboundHeader.getStartOrderDate();
            startOrderDate = DateUtils.truncate(startOrderDate, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REF_DOC_DATE").$greater$eq(dateFormat.format(startOrderDate)));
        }
        if (searchPreOutboundHeader.getEndOrderDate() != null) {
            Date endOrderDate = searchPreOutboundHeader.getEndOrderDate();
            endOrderDate = DateUtils.ceiling(endOrderDate, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("REF_DOC_DATE").$less$eq(dateFormat.format(endOrderDate)));
        }
        if (searchPreOutboundHeader.getStartCreatedOn() != null) {
            Date startCreatedOn = searchPreOutboundHeader.getStartCreatedOn();
            startCreatedOn = DateUtils.truncate(startCreatedOn, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PRE_OB_CTD_ON").$greater$eq(dateFormat.format(startCreatedOn)));
        }
        if (searchPreOutboundHeader.getEndCreatedOn() != null) {
            Date endCreatedOn = searchPreOutboundHeader.getEndCreatedOn();
            endCreatedOn = DateUtils.ceiling(endCreatedOn, Calendar.DAY_OF_MONTH);
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PRE_OB_CTD_ON").$less$eq(dateFormat.format(endCreatedOn)));
        }

        //v2 filed
        if(searchPreOutboundHeader.getCompanyCodeId() != null && !searchPreOutboundHeader.getCompanyCodeId().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3 .filter(col("C_ID").isin(searchPreOutboundHeader.getCompanyCodeId().toArray()));
        }
        if(searchPreOutboundHeader.getLanguageId() != null && !searchPreOutboundHeader.getLanguageId().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("LANG_ID").isin(searchPreOutboundHeader.getLanguageId().toArray()));
        }
        if(searchPreOutboundHeader.getPlantId() != null && !searchPreOutboundHeader.getPlantId().isEmpty()){
            preObHeaderQueryV3 = preObHeaderQueryV3.filter(col("PLANT_ID").isin(searchPreOutboundHeader.getPlantId().toArray()));
        }

        Encoder<PreOutboundHeader> preOutboundHeaderEncoder = Encoders.bean(PreOutboundHeader.class);
        Dataset<PreOutboundHeader> dataSetControlGroup = preObHeaderQueryV3.as(preOutboundHeaderEncoder);
        List<PreOutboundHeader> result = dataSetControlGroup.collectAsList();

        return result;
    }
}
