package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PerpetualHeader;
import com.mnrclara.spark.core.model.wmscore.SearchPerpetualHeader;
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
public class PerpetualHeaderServiceV2 {
    Properties connProp = new Properties();
    SparkSession sparkSession = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public PerpetualHeaderServiceV2() {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PerpetualHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblperpetualheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblperpetualheaderv3");
    }

    //FindPerpetualHeader
    public List<PerpetualHeader> findPerpetualHeaderV3(SearchPerpetualHeader searchPerpetualHeader) throws ParseException {

        Dataset<Row> perpetualheaderQueryV3 =sparkSession.sql( "SELECT " +
                "LANG_ID as languageId, " +
                "C_ID as companyCodeId, " +
                "PLANT_ID as plantId, " +
                "WH_ID as warehouseId, " +
                "CC_TYP_ID as cycleCountTypeId, " +
                "CC_NO as cycleCountNo, " +
                "MVT_TYP_ID as movementTypeId, " +
                "SUB_MVT_TYP_ID as subMovementTypeId, " +
                "STATUS_ID as statusId, " +
                "REF_FIELD_1 as referenceField1, " +
                "REF_FIELD_2 as referenceField2, " +
                "REF_FIELD_3 as referenceField3, " +
                "REF_FIELD_4 as referenceField4, " +
                "REF_FIELD_5 as referenceField5, " +
                "REF_FIELD_6 as referenceField6, " +
                "REF_FIELD_7 as referenceField7, " +
                "REF_FIELD_8 as referenceField8, " +
                "REF_FIELD_9 as referenceField9, " +
                "REF_FIELD_10 as referenceField10, " +
                "IS_DELETED as deletionIndicator, " +
                "CC_CTD_BY as createdBy, " +
                "CC_CTD_ON as createdOn, " +
                "CC_CNT_BY as countedBy, " +
                "CC_CNT_ON as countedOn, " +
                "CC_CNF_BY as confirmedBy, " +
                "CC_CNF_ON as confirmedOn " +
                "FROM tblperpetualheaderv3 WHERE IS_DELETED = 0 ");

        perpetualheaderQueryV3.cache();

        if (searchPerpetualHeader.getWarehouseId() != null && !searchPerpetualHeader.getWarehouseId().isEmpty()) {
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("WH_ID").isin(searchPerpetualHeader.getWarehouseId().toArray()));
        }
        if (searchPerpetualHeader.getCycleCountTypeId() != null && !searchPerpetualHeader.getCycleCountTypeId().isEmpty()) {
            List<String> cycleCountString = searchPerpetualHeader.getCycleCountTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("CC_TYP_ID").isin(cycleCountString.toArray()));
        }
        if (searchPerpetualHeader.getCycleCountNo() != null && !searchPerpetualHeader.getCycleCountNo().isEmpty()) {
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("CC_NO").isin(searchPerpetualHeader.getCycleCountNo().toArray()));
        }
        if (searchPerpetualHeader.getMovementTypeId() != null && !searchPerpetualHeader.getMovementTypeId().isEmpty()) {
            List<String> movementTypeIdString = searchPerpetualHeader.getMovementTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("MVT_TYP_ID").isin(movementTypeIdString.toArray()));
        }
        if (searchPerpetualHeader.getSubMovementTypeId() != null && !searchPerpetualHeader.getSubMovementTypeId().isEmpty()) {
            List<String> subMovementTypeIdString = searchPerpetualHeader.getMovementTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("SUB_MVT_TYP_ID").isin(subMovementTypeIdString.toArray()));
        }
        if (searchPerpetualHeader.getHeaderStatusId() != null && !searchPerpetualHeader.getHeaderStatusId().isEmpty()) {
            List<String> headerStatusIdString = searchPerpetualHeader.getHeaderStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("STATUS_ID").isin(headerStatusIdString.toArray()));
        }
        if (searchPerpetualHeader.getLineStatusId() != null && !searchPerpetualHeader.getLineStatusId().isEmpty()) {
            List<String> lineStatusIdString = searchPerpetualHeader.getLineStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("STATUS_ID").isin(lineStatusIdString.toArray()));
        }
        if (searchPerpetualHeader.getCreatedBy() != null && !searchPerpetualHeader.getCreatedBy().isEmpty()) {
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("CC_CTD_BY").isin(searchPerpetualHeader.getCreatedBy().toArray()));
        }
        if(searchPerpetualHeader.getStartCreatedOn() != null){
            Date fromCreatedOn = searchPerpetualHeader.getStartCreatedOn();
            fromCreatedOn = DateUtils.truncate(fromCreatedOn, Calendar.DAY_OF_MONTH);
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("CC_CTD_ON").$greater$eq(dateFormat.format(fromCreatedOn)));
        }
        if(searchPerpetualHeader.getEndCreatedOn() != null){
            Date toCreatedOn = searchPerpetualHeader.getEndCreatedOn();
            toCreatedOn = DateUtils.ceiling(toCreatedOn, Calendar.DAY_OF_MONTH);
            perpetualheaderQueryV3 = perpetualheaderQueryV3.filter(col("CC_CTD_ON").$less$eq(dateFormat.format(toCreatedOn)));
        }
        Encoder<PerpetualHeader> perpetualHeaderEncoder = Encoders.bean(PerpetualHeader.class);
        Dataset<PerpetualHeader> perpetualHeaderDataset = perpetualheaderQueryV3.as(perpetualHeaderEncoder);
        List<PerpetualHeader> result = perpetualHeaderDataset.collectAsList();

        return result;
    }
}
