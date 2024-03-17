package com.mnrclara.spark.core.service.almailem.joinspark;


import com.mnrclara.spark.core.model.Almailem.FindInboundHeaderV2;
import com.mnrclara.spark.core.model.Almailem.InboundHeaderV2;
import com.mnrclara.spark.core.model.Almailem.joinspark.InboundHeaderV3;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.sum;


@Service
@Slf4j
public class IBHeaderJoinService {

    private Properties CONN_PROP = new Properties();

    private SparkSession sparkSession;
    private Dataset<Row> ibTbl;
    private Dataset<Row> ibLineTbl;

    private Dataset<Row> joinVal;
    private Dataset<Row> filterVal;

    public IBHeaderJoinService() throws ParseException {
        CONN_PROP.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        CONN_PROP.put("user", "sa");
        CONN_PROP.put("password", "Do1cavIFK4^$pQ^zZYsX");
        sparkSession = SparkSession.builder().master("local[*]").appName("Inboundheader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        ibTbl = readTable("tblinboundheader");
        ibTbl.createOrReplaceTempView("tblinboundheaderv2");
        ibLineTbl = readTable("tblinboundline");
        ibLineTbl.createOrReplaceTempView("tblinboundlinev2");

//        Dataset<Row> totalQtyDF = ibLineTbl.groupBy("REF_DOC_NO")
//                .agg(sum("ACCEPT_QTY").as("totalAcceptQty"), sum("DAMAGE_QTY").as("totalDamageQty"), sum("ORD_QTY").as("totalOrderQty"));

//        joinVal = joinVal.join(totalQtyDF, joinVal.col("REF_DOC_NO").equalTo(totalQtyDF.col("REF_DOC_NO")), "left_outer")
//                .drop(totalQtyDF.col("REF_DOC_NO"));

        ibLineTbl.groupBy("REF_DOC_NO")
                .agg(sum("ACCEPT_QTY").as("totalAcceptQty"), sum("DAMAGE_QTY").as("totalDamageQty"), sum("ORD_QTY").as("totalOrderQty"))
                .repartition(16)
                .createOrReplaceTempView("tblinboundlinev2");

        // Perform left join between ibTbl and ibLineTbl on "REF_DOC_NO" with column aliases
        joinVal = ibTbl.alias("ibTbl")
                .join(ibLineTbl.alias("ibLineTbl"), col("ibTbl.REF_DOC_NO").equalTo(col("ibLineTbl.REF_DOC_NO")), "left_outer");

        // Perform left join between ibTbl and ibLineTbl on "REF_DOC_NO"
//        joinVal = ibTbl.join(ibLineTbl, ibTbl.col("REF_DOC_NO").equalTo(ibLineTbl.col("REF_DOC_NO")), "left_outer");
    }




    private Dataset<Row> readTable(String tableName){
        return sparkSession.read().option("fetchSize", "10000")
                .jdbc("jdbc:sqlserver://3.109.20.248;databaseName=WMS_ALMDEV", tableName, CONN_PROP)
                .repartition(16);
    }

    // FindInboundHeader
    public List<InboundHeaderV3> findInboundHeader(FindInboundHeaderV2 findInboundHeaderV2) throws Exception{

    filterVal = joinVal ;


        if(findInboundHeaderV2.getCompanyCodeId() !=null && !findInboundHeaderV2.getCompanyCodeId().isEmpty()){
            filterVal = filterVal.filter(col("c_id").isin(findInboundHeaderV2.getCompanyCodeId().toArray(new String[0]))).toDF();
        }
        if(findInboundHeaderV2.getPlantId() != null && !findInboundHeaderV2.getPlantId().isEmpty()){
            filterVal = filterVal.filter(col("plant_id").isin(findInboundHeaderV2.getPlantId().toArray(new String[0]))).toDF();
        }
        Encoder<InboundHeaderV3> inboundHeaderV2Encoder = Encoders.bean(InboundHeaderV3.class);
        Dataset<InboundHeaderV3> inboundHeaderV2Dataset = filterVal.as(inboundHeaderV2Encoder);

        return inboundHeaderV2Dataset.collectAsList();

    }





}
