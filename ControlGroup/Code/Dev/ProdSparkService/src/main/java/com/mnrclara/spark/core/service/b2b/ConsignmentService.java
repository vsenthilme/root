package com.mnrclara.spark.core.service.b2b;


import com.mnrclara.spark.core.model.b2b.Consignment;
import com.mnrclara.spark.core.model.b2b.FindConsignment;
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
import org.apache.spark.sql.functions;

import static org.apache.spark.sql.functions.col;


@Service
@Slf4j
public class ConsignmentService {


    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ConsignmentService() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("Consignment.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df1 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=IWE", "tblconsignment", connProp)
                .repartition(16);
        df1.createOrReplaceTempView("tblconsignment");

//        Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=IWE", "tblconsignmentwebhook", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblconsignmentwebhook");

        //Read from Sql Table
        val df3 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=IWE", "tbljntwebhook", connProp)
                .repartition(16);
        df3.createOrReplaceTempView("tbljntwebhook");

    }

    /**
     *
     * @param findConsignment
     * @return
     * @throws ParseException
     */
    public List<Consignment> findConsignment(FindConsignment findConsignment) throws ParseException {


        Dataset<Row> consignmentQuery = sparkSession.sql("SELECT \n" +
                "tc.CONSIGNMENT_ID AS consignmentId, \n" +
                "tc.REFERENCE_NUMBER AS referenceNumber, \n" +
                "tc.COD_AMOUNT AS codAmount, \n" +
                "tc.COD_COLLECTION_MODE AS codCollectionMode, \n" +
                "tc.CUSTOMER_CODE AS customerCode, \n" +
                "tc.SERVICE_TYPE_ID AS serviceTypeId, \n" +
                "tc.CONSIGNMENT_TYPE AS consignmentType, \n" +
                "tc.LOAD_TYPE AS loadType, \n" +
                "tc.DESCRIPTION AS description, \n" +
                "tc.COD_FAVOR_OF AS codFavorOf, \n" +
                "tc.DIMENSION_UNIT AS dimensionUnit, \n" +
                "tc.LENGTH AS length, \n" +
                "tc.WIDTH AS width, \n" +
                "tc.HEIGHT AS height, \n" +
                "tc.WEIGHT_UNIT AS weightUnit, \n" +
                "tc.WEIGHT AS weight, \n" +
                "tc.DECLARED_VALUE AS declaredValue, \n" +
                "tc.NUM_PIECES AS numPieces, \n" +
                "tc.NOTES AS notes, \n" +
                "tc.CUSTOMER_REFERENCE_NUMBER AS customerReferenceNumber, \n" +
                "tc.IS_RISK_SURCHARGE_APPLICABLE AS isRiskSurchargeApplicable, \n" +
                "tc.CREATED_AT AS createdAt, \n" +
                "tc.STATUS_DESCRIPTION AS statusDescription, \n" +
                "tc.CUSTOMER_CIVIL_ID AS customerCivilId, \n" +
                "tc.RECEIVER_CIVIL_ID AS receiverCivilId, \n" +
                "tc.CURRENCY AS currency, \n" +
                "tc.AWB_3RD_PARTY AS awb3rdParty, \n" +
                "tc.SCANTYPE_3RD_PARTY AS scanType3rdParty, \n" +
                "tc.HUBCODE_3RD_PARTY AS hubCode3rdParty, \n" +
                "tc.ORDER_TYPE AS orderType, \n" +
                "tc.JNT_PUSH_STATUS AS jntPushStatus, \n" +
                "tc.BOUTIQAAT_PUSH_STATUS AS boutiqaatPushStatus, \n" +
                "tc.IS_AWB_PRINTED AS isAwbPrinted, \n" +
                "jw.SCAN_TYPE AS scanType, \n" +
                "tcw.HUB_CODE AS hubCode \n" +
                "FROM tblconsignment tc \n" +
                "LEFT JOIN tblconsignmentwebhook tcw ON tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER \n" +
                "LEFT OUTER JOIN tbljntwebhook jw ON jw.BILL_CODE = tc.AWB_3RD_PARTY ");



//        if(findConsignment.getReference_number() != null && !findConsignment.getReference_number().isEmpty()){
//            consignmentQuery = consignmentQuery.filter(
//                    functions.coalesce(col("referenceNumber"), functions.lit("null")).isNull()
//                            .or(functions.array_contains(col("referenceNumber"), findConsignment.getReference_number().toArray())));
//        }
        if (findConsignment.getReference_number() != null && !findConsignment.getReference_number().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(col("tc.REFERENCE_NUMBER").isin(findConsignment.getReference_number().toArray()));
        }

//        if(findConsignment.getReference_number() != null && !findConsignment.getReference_number().isEmpty()){
//            consignmentQuery = consignmentQuery.filter(
//                    functions.coalesce(col("tc.REFERENCE_NUMBER"), functions.lit("null")).isNull()
//                            .or(functions.array_contains(col("tc.REFERENCE_NUMBER"), findConsignment.getReference_number().toArray())));
//        }

        if (findConsignment.getAwb_3rd_Party() != null && !findConsignment.getAwb_3rd_Party().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    functions.coalesce(col("tc.awb_3rd_Party"), functions.lit("null")).isNull()
                            .or(functions.array_contains(col("tc.awb_3rd_Party"), findConsignment.getAwb_3rd_Party().toArray())));
        }

        if (findConsignment.getBoutiqaatPushStatus() != null && !findConsignment.getBoutiqaatPushStatus().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    functions.coalesce(col("tc.BOUTIQAAT_PUSH_STATUS"), functions.lit("null")).isNull()
                            .or(functions.array_contains(col("tc.BOUTIQAAT_PUSH_STATUS"), findConsignment.getBoutiqaatPushStatus().toArray())));
        }
//        if (findConsignment.getReference_number() != null && !findConsignment.getReference_number().isEmpty()) {
//            consignmentQuery = consignmentQuery.filter(
//                    col("tc.referenceNumber").isin(findConsignment.getReference_number().toArray()));
//                           .or(col("tc.referenceNumber")).isNull());
//        }

        if (findConsignment.getCustomer_code() != null && !findConsignment.getCustomer_code().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.customer_code").isin(findConsignment.getCustomer_code().toArray())
                            .or(col("tc.customer_code").isNull()));
        }

        if (findConsignment.getConsignment_type() != null && !findConsignment.getConsignment_type().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.consignment_type").isin(findConsignment.getConsignment_type().toArray())
                            .or(col("tc.consignment_type").isNull()));
        }

        if (findConsignment.getCustomer_reference_number() != null && !findConsignment.getCustomer_reference_number().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.customer_reference_number").isin(findConsignment.getCustomer_reference_number().toArray())
                            .or(col("tc.customer_reference_number").isNull()));
        }

        if (findConsignment.getCustomer_civil_id() != null && !findConsignment.getCustomer_civil_id().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.customer_civil_id").isin(findConsignment.getCustomer_civil_id().toArray())
                            .or(col("tc.customer_civil_id").isNull()));
        }

        if (findConsignment.getReceiver_civil_id() != null && !findConsignment.getReceiver_civil_id().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.receiver_civil_id").isin(findConsignment.getReceiver_civil_id().toArray())
                            .or(col("tc.receiver_civil_id").isNull()));
        }

        if (findConsignment.getAwb_3rd_Party() != null && !findConsignment.getAwb_3rd_Party().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.awb_3rd_Party").isin(findConsignment.getAwb_3rd_Party().toArray())
                            .or(col("tc.awb_3rd_Party").isNull()));
        }

        if (findConsignment.getScanType_3rd_Party() != null && !findConsignment.getScanType_3rd_Party().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.scanType_3rd_Party").isin(findConsignment.getScanType_3rd_Party().toArray())
                            .or(col("tc.scanType_3rd_Party").isNull()));
        }

        if (findConsignment.getHubCode_3rd_Party() != null && !findConsignment.getHubCode_3rd_Party().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.hubCode_3rd_Party").isin(findConsignment.getHubCode_3rd_Party().toArray())
                            .or(col("tc.hubCode_3rd_Party").isNull()));
        }

        if (findConsignment.getOrderType() != null && !findConsignment.getOrderType().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.order_type").isin(findConsignment.getOrderType().toArray())
                            .or(col("tc.order_type").isNull()));
        }

        if (findConsignment.getJntPushStatus() != null && !findConsignment.getJntPushStatus().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.jnt_push_status").isin(findConsignment.getJntPushStatus().toArray())
                            .or(col("tc.jnt_push_status").isNull()));
        }

        if (findConsignment.getBoutiqaatPushStatus() != null && !findConsignment.getBoutiqaatPushStatus().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tc.boutiqaat_push_status").isin(findConsignment.getBoutiqaatPushStatus().toArray())
                            .or(col("tc.boutiqaat_push_status").isNull()));
        }

        if (findConsignment.getScanType() != null && !findConsignment.getScanType().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("jw.scan_type").isin(findConsignment.getScanType().toArray())
                            .or(col("jw.scan_type").isNull()));
        }

        if (findConsignment.getHubCode() != null && !findConsignment.getHubCode().isEmpty()) {
            consignmentQuery = consignmentQuery.filter(
                    col("tcw.hub_code").isin(findConsignment.getHubCode().toArray())
                            .or(col("tcw.hub_code").isNull()));
        }

        if (findConsignment.getStartDate() != null) {
            Date startDate = findConsignment.getStartDate();
            startDate = org.apache.commons.lang3.time.DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            consignmentQuery = consignmentQuery.filter(col("tc.created_at").$greater$eq(dateFormat.format(startDate)));
        }

        if (findConsignment.getEndDate() != null) {
            Date endDate = findConsignment.getEndDate();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            consignmentQuery = consignmentQuery.filter(col("tc.created_at").$less$eq(dateFormat.format(endDate)));
        }
        Encoder<Consignment> consignmentEncoder = Encoders.bean(Consignment.class);
        Dataset<Consignment> consignmentDataset = consignmentQuery.as(consignmentEncoder);
        List<Consignment> result = consignmentDataset.collectAsList();
        return result;
    }
}



