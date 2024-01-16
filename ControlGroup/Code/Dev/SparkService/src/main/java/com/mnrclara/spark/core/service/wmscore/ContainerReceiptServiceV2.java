package com.mnrclara.spark.core.service.wmscore;



import com.mnrclara.spark.core.model.wmscore.ContainerReceipt;
import com.mnrclara.spark.core.model.wmscore.SearchContainerReceipt;
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
public class ContainerReceiptServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ContainerReceiptServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("ContainerReceipt.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblcontainerreceipt", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblcontainerreceiptv3");

    }

    public List<ContainerReceipt> findContainerReceiptV3(SearchContainerReceipt searchContainerReceipt) throws ParseException{
        Dataset<Row>
                containerReceiptQueryV3 =sparkSession.sql( "SELECT " +
                " LANG_ID as languageId, " +
                " C_ID as companyCodeId, " +
                " PLANT_ID as plantId, " +
                " WH_ID as warehouseId, " +
                " PRE_IB_NO as preInboundNo, " +
                " REF_DOC_NO as refDocNumber, " +
                " CONT_REC_NO as containerReceiptNo, " +
                " CONT_REC_DATE as containerReceivedDate, " +
                " CONT_NO as containerNo, " +
                " STATUS_ID as statusId, " +
                " CONT_TYP as containerType, " +
                " PARTNER_CODE as partnerCode, " +
                " INV_NO as invoiceNo, " +
                " CONS_TYPE as consignmentType, " +
                " ORIGIN as origin, " +
                " PAL_QTY as numberOfPallets, " +
                " CASE_NO as numberOfCases, " +
                " DOCK_ALL_NO as dockAllocationNo, " +
                " REMARK as remarks, " +
                " IS_DELETED as deletionIndicator, " +
                " REF_FIELD_1 as referenceField1, " +
                " REF_FIELD_2 as referenceField2, " +
                " REF_FIELD_3 as referenceField3, " +
                " REF_FIELD_4 as referenceField4, " +
                " REF_FIELD_5 as referenceField5, " +
                " REF_FIELD_6 as referenceField6, " +
                " REF_FIELD_7 as referenceField7, " +
                " REF_FIELD_8 as referenceField8, " +
                " REF_FIELD_9 as referenceField9, " +
                " REF_FIELD_10 as referenceField10, " +
                " CTD_BY as createdBy, " +
                " CTD_ON as createdOn, " +
                " UTD_BY as updatedBy, " +
                " UTD_ON as updatedOn, " +
                " COMPANY_DESC as companyDescription, "
                + "PLANT_DESC as plantDescription, "
                + "WAREHOUSE_DESC as warehouseDescription "
                + "FROM tblcontainerreceiptv3 WHERE IS_DELETED = 0 ");

        containerReceiptQueryV3.cache();

        if (searchContainerReceipt.getLanguageId() != null && !searchContainerReceipt.getLanguageId().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("LANG_ID").isin(searchContainerReceipt.getLanguageId().toArray()));
        }
        if (searchContainerReceipt.getCompanyCodeId() != null && !searchContainerReceipt.getCompanyCodeId().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("C_ID").isin(searchContainerReceipt.getCompanyCodeId().toArray()));
        }
        if (searchContainerReceipt.getPlantId() != null && !searchContainerReceipt.getPlantId().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("PLANT_ID").isin(searchContainerReceipt.getPlantId().toArray()));
        }
        if (searchContainerReceipt.getWarehouseId() != null && !searchContainerReceipt.getWarehouseId().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("WH_ID").isin(searchContainerReceipt.getWarehouseId().toArray()));
        }
        if (searchContainerReceipt.getContainerReceiptNo() != null && !searchContainerReceipt.getContainerReceiptNo().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CONT_REC_NO").isin(searchContainerReceipt.getContainerReceiptNo().toArray()));
        }
        if (searchContainerReceipt.getContainerNo() != null && !searchContainerReceipt.getContainerNo().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CONT_NO").isin(searchContainerReceipt.getContainerNo().toArray()));
        }
        if (searchContainerReceipt.getPartnerCode() != null && !searchContainerReceipt.getPartnerCode().isEmpty()) {
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("PARTNER_CODE").isin(searchContainerReceipt.getPartnerCode().toArray()));
        }
//        if (searchContainerReceipt.getUnloadedBy() != null && !searchContainerReceipt.getUnloadedBy().isEmpty()) {
//            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("REF_FIELD_1").isin(searchContainerReceipt.getUnloadedBy().toArray()));
//        }
        if (searchContainerReceipt.getStatusId() != null && !searchContainerReceipt.getStatusId().isEmpty()) {
            List<String> statusString = searchContainerReceipt.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("STATUS_ID").isin(statusString.toArray()));
        }
        if (searchContainerReceipt.getStartContainerReceivedDate() != null) {
            Date startContainerReceiveDate = searchContainerReceipt.getStartContainerReceivedDate();
            startContainerReceiveDate = DateUtils.truncate(startContainerReceiveDate, Calendar.DAY_OF_MONTH);
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CONT_REC_DATE").$greater$eq(dateFormat.format(startContainerReceiveDate)));
        }
        if (searchContainerReceipt.getEndContainerReceivedDate() != null) {
            Date endContainerReceiveDate = searchContainerReceipt.getEndContainerReceivedDate();
            endContainerReceiveDate = DateUtils.ceiling(endContainerReceiveDate, Calendar.DAY_OF_MONTH);
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CONT_REC_DATE").$less$eq(dateFormat.format(endContainerReceiveDate)));
        }
        if(searchContainerReceipt.getFromCreatedOn() != null){
            Date fromCreatedOn = searchContainerReceipt.getFromCreatedOn();
            fromCreatedOn = DateUtils.truncate(fromCreatedOn, Calendar.DAY_OF_MONTH);
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CTD_ON").$greater$eq(dateFormat.format(fromCreatedOn)));
        }
        if(searchContainerReceipt.getToCreatedOn() != null){
            Date toCreatedOn = searchContainerReceipt.getToCreatedOn();
            toCreatedOn = DateUtils.ceiling(toCreatedOn, Calendar.DAY_OF_MONTH);
            containerReceiptQueryV3 = containerReceiptQueryV3.filter(col("CTD_ON").$less$eq(dateFormat.format(toCreatedOn)));
        }
        Encoder<ContainerReceipt> containerReceiptEncoder = Encoders.bean(ContainerReceipt.class);
        Dataset<ContainerReceipt> dataSetControlGroup = containerReceiptQueryV3.as(containerReceiptEncoder);
        List<ContainerReceipt> result = dataSetControlGroup.collectAsList();

        return result;
    }

}