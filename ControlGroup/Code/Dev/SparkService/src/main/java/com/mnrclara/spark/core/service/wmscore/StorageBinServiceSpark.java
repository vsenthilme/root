package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.FindStorageBin;
import com.mnrclara.spark.core.model.wmscore.StorageBin;
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
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;


@Service
@Slf4j
public class StorageBinServiceSpark {

    Properties conProp = new Properties();

    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public StorageBinServiceSpark() throws ParseException {
        //connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("StorageBin.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblstoragebin", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblstoragebin3");

    }



    /**
     *
     * @param findStorageBin
     * @return
     * @throws ParseException
     */
    public List<StorageBin> findStorageBin(FindStorageBin findStorageBin) throws ParseException {


        Dataset<Row> storageBinQuery = sparkSession.sql("SELECT "
                + "ST_BIN as storageBin, "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "FL_ID as floorId, "
                + "LEVEL_ID as levelId, "
                + "ST_SEC_ID as storageSectionId, "
                + "ROW_ID as rowId, "
                + "AISLE_ID as aisleNumber, "
                + "SPAN_ID as spanId, "
                + "SHELF_ID as shelfId, "
                + "BIN_SECTION_ID as binSectionId, "
                + "ST_TYP_ID as storageTypeId, "
                + "BIN_CL_ID as binClassId, "
                + "OCC_VOL as occupiedVolume, "
                + "OCC_WT as occupiedWeight, "
                + "OCC_QTY as occupiedQuantity, "
                + "REMAIN_VOL as remainingVolume, "
                + "REMAIN_WT as remainingWeight, "
                + "REMAIN_QTY as remainingQuantity, "
                + "TOT_VOL as totalVolume, "
                + "TOT_QTY as totalQuantity, "
                + "TOT_WT as totalWeight, "
                + "ST_BIN_TEXT as description, "
                + "BIN_BAR as binBarcode, "
                + "PUTAWAY_BLOCK as putawayBlock, "
                + "PICK_BLOCK as pickingBlock, "
                + "BLK_REASON as blockReason, "
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
                + "CTD_BY as createdBy, "
                + "CTD_ON as createdOn, "
                + "UTD_ON as updatedOn, "
                + "UTD_BY as updatedBy "
                + "FROM tblstoragebin3 WHERE IS_DELETED = 0");

        storageBinQuery.cache();

        if (findStorageBin.getAisleNumber() != null && !findStorageBin.getAisleNumber().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("AISLE_ID").isin(findStorageBin.getAisleNumber().toArray()));
        }
        if (findStorageBin.getStorageBin() != null && !findStorageBin.getStorageBin().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("ST_BIN").isin(findStorageBin.getStorageBin().toArray()));
        }
        if (findStorageBin.getWarehouseId() != null && !findStorageBin.getWarehouseId().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("WH_ID").isin(findStorageBin.getWarehouseId().toArray()));
        }
        if (findStorageBin.getFloorId() != null && !findStorageBin.getFloorId().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("FL_ID").isin(findStorageBin.getFloorId().toArray()));
        }
        if (findStorageBin.getStorageSectionId() != null && !findStorageBin.getStorageSectionId().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("ST_SEC_ID").isin(findStorageBin.getStorageSectionId().toArray()));
        }
        if (findStorageBin.getRowId() != null && !findStorageBin.getRowId().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("ROW_ID").isin(findStorageBin.getRowId().toArray()));
        }
        if (findStorageBin.getLanguageId() != null && !findStorageBin.getLanguageId().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("LANG_ID").isin(findStorageBin.getLanguageId().toArray()));
        }
        if(findStorageBin.getCompanyCodeId() != null && !findStorageBin.getCompanyCodeId().isEmpty()){
            storageBinQuery = storageBinQuery.filter(col("C_ID").isin(findStorageBin.getCompanyCodeId().toArray()));
        }
        if(findStorageBin.getPlantId() != null && !findStorageBin.getPlantId().isEmpty()){
            storageBinQuery = storageBinQuery.filter(col("PLANT_ID").isin(findStorageBin.getPlantId().toArray()));
        }
        if(findStorageBin.getSpanId() != null && !findStorageBin.getSpanId().isEmpty()){
            storageBinQuery = storageBinQuery.filter(col("SPAN_ID").isin(findStorageBin.getSpanId().toArray()));
        }
        if(findStorageBin.getShelfId() != null && !findStorageBin.getShelfId().isEmpty()){
            storageBinQuery = storageBinQuery.filter(col("SHELF_ID").isin(findStorageBin.getShelfId().toArray()));
        }

        if (findStorageBin.getStatusId() != null && !findStorageBin.getStatusId().isEmpty()) {
            List<String> stringList = findStorageBin.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            storageBinQuery = storageBinQuery.filter(col("STATUS_ID").isin(stringList.toArray()));
        }

        if (findStorageBin.getCreatedBy() != null && !findStorageBin.getCreatedBy().isEmpty()) {
            storageBinQuery = storageBinQuery.filter(col("CTD_BY").isin(findStorageBin.getCreatedBy().toArray()));
        }

        if (findStorageBin.getStartCreatedOn() != null) {
            Date startDate = findStorageBin.getStartCreatedOn();
            startDate = org.apache.commons.lang3.time.DateUtils.ceiling(startDate, Calendar.DAY_OF_MONTH);
            storageBinQuery = storageBinQuery.filter(col("CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (findStorageBin.getEndCreatedOn() != null) {
            Date endDate = findStorageBin.getEndCreatedOn();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            storageBinQuery = storageBinQuery.filter(col("CTD_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (findStorageBin.getStartUpdatedOn() != null) {
            Date endDate = findStorageBin.getStartUpdatedOn();
            endDate = org.apache.commons.lang3.time.DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
            storageBinQuery = storageBinQuery.filter(col("UTD_ON").$less$eq(dateFormat.format(endDate)));
        }
        if (findStorageBin.getEndCreatedOn() != null) {
            Date endDate = findStorageBin.getEndCreatedOn();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            storageBinQuery = storageBinQuery.filter(col("UTD_ON").$less$eq(dateFormat.format(endDate)));
        }

        Encoder<StorageBin> storageBinEncoder = Encoders.bean(StorageBin.class);
        Dataset<StorageBin> dataSetControlGroup = storageBinQuery.as(storageBinEncoder);
        List<StorageBin> result = dataSetControlGroup.collectAsList();

        return result;
    }

}
