package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.FindPutAwayHeader;
import com.mnrclara.spark.core.model.wmscore.PutAwayHeader;
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
public class PutAwayHeaderServiceSpark {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PutAwayHeaderServiceSpark() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PutAwayHeader.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblputawayheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblputawayheaderV3");

    }


    /**
     *
     * @param findPutAwayHeader
     * @return
     * @throws ParseException
     */
    public List<PutAwayHeader> findPutAwayHeader(FindPutAwayHeader findPutAwayHeader) throws ParseException {

        Dataset<Row> imPutawayHeaderQueryV2 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_IB_NO as preInboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "GR_NO as goodsReceiptNo, "
                + "IB_ORD_TYP_ID as inboundOrderTypeId, "
                + "PAL_CODE as palletCode, "
                + "CASE_CODE as caseCode, "
                + "PACK_BARCODE as packBarcodes, "
                + "PA_NO as putAwayNumber, "
                + "PROP_ST_BIN as proposedStorageBin, "
                + "PA_QTY as putAwayQuantity, "
                + "PA_UOM as putAwayUom, "
                + "STR_TYP_ID as strategyTypeId, "
                + "ST_NO as strategyNo, "
                + "PROP_HE_NO as proposedHandlingEquipment, "
                + "ASS_USER_ID as assignedUserId, "
                + "STATUS_ID as statusId, "
                + "QTY_TYPE as quantityType, "
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
                + "PA_CTD_BY as createdBy, "
                + "PA_CTD_ON as createdOn, "
                + "PA_UTD_BY as updatedBy, "
                + "PA_UTD_ON as updatedOn, "
                + "PA_CNF_BY as confirmedBy, "
                + "PA_CNF_ON as confirmedOn "
//                + "INV_QTY as inventoryQuantity, "
//                + "BARCODE_ID as barcodeId, "
//                + "MFR_DATE as manufacturerDate, "
//                + "EXP_DATE as expiryDate, "
//                + "MFR_CODE as manufacturerCode, "
//                + "MFR_NAME as manufacturerName, "
//                + "ORIGIN as origin, "
//                + "BRAND as brand, "
//                + "ORD_QTY as orderQty, "
//                + "CBM as cbm, "
//                + "CBM_UNIT as cbmUnit, "
//                + "CBM_QTY as cbmQuantity, "
//                + "APP_STATUS as approvalStatus, "
//                + "REMARK as remark, "
//                + "C_TEXT as companyDescription, "
//                + "PLANT_TEXT as plantDescription, "
//                + "WH_TEXT as warehouseDescription, "
//                + "STATUS_TEXT as statusDescription "
                + "FROM tblputawayheaderV3 WHERE IS_DELETED = 0");

        if (findPutAwayHeader.getWarehouseId() != null && !findPutAwayHeader.getWarehouseId().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("WH_ID").isin(findPutAwayHeader.getWarehouseId().toArray()));
        }
        if (findPutAwayHeader.getRefDocNumber() != null && !findPutAwayHeader.getRefDocNumber().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("REF_DOC_NO").isin(findPutAwayHeader.getRefDocNumber().toArray()));
        }
        if (findPutAwayHeader.getPackBarcodes() != null && !findPutAwayHeader.getPackBarcodes().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PACK_BARCODE").isin(findPutAwayHeader.getPackBarcodes().toArray()));
        }
        if (findPutAwayHeader.getPutAwayNumber() != null && !findPutAwayHeader.getPutAwayNumber().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PA_NO").isin(findPutAwayHeader.getPutAwayNumber().toArray()));
        }
        if (findPutAwayHeader.getProposedStorageBin() != null && !findPutAwayHeader.getProposedStorageBin().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PROP_ST_BIN").isin(findPutAwayHeader.getProposedStorageBin().toArray()));
        }
        if (findPutAwayHeader.getProposedHandlingEquipment() != null && !findPutAwayHeader.getProposedHandlingEquipment().isEmpty()) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PROP_HE_NO").isin(findPutAwayHeader.getProposedHandlingEquipment().toArray()));
        }
        if (findPutAwayHeader.getStatusId() != null && !findPutAwayHeader.getStatusId().isEmpty()) {
            List<String> statusIdStrings = findPutAwayHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }
        if (findPutAwayHeader.getCreatedBy() != null) {
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PA_CTD_BY").isin(findPutAwayHeader.getCreatedBy().toArray()));
        }
        if (findPutAwayHeader.getStartCreatedOn() != null) {
            Date startDate = findPutAwayHeader.getStartCreatedOn();
            startDate = org.apache.commons.lang3.time.DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PA_CTD_ON").$greater$eq(dateFormat.format(startDate)));
        }
        if (findPutAwayHeader.getEndCreatedOn() != null) {
            Date endDate = findPutAwayHeader.getEndCreatedOn();
            endDate = org.apache.commons.lang3.time.DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PA_CTD_ON").$less$eq(dateFormat.format(endDate)));
        }

        //v2 fields
        if(findPutAwayHeader.getCompanyCodeId() != null && !findPutAwayHeader.getCompanyCodeId().isEmpty()){
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("C_ID").isin(findPutAwayHeader.getCompanyCodeId().toArray()));
        }
        if(findPutAwayHeader.getPlantId() != null && !findPutAwayHeader.getPlantId().isEmpty()){
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("PLANT_ID").isin(findPutAwayHeader.getPlantId().toArray()));
        }
        if(findPutAwayHeader.getLanguageId() != null && !findPutAwayHeader.getLanguageId().isEmpty()){
            imPutawayHeaderQueryV2 = imPutawayHeaderQueryV2.filter(col("LANG_ID").isin(findPutAwayHeader.getLanguageId().toArray()));
        }
        Encoder<PutAwayHeader> putAwayHeaderEncoderV2 = Encoders.bean(PutAwayHeader.class);
        Dataset<PutAwayHeader> dataSetControlGroup = imPutawayHeaderQueryV2.as(putAwayHeaderEncoderV2);
        List<PutAwayHeader> result = dataSetControlGroup.collectAsList();

        return result;
    }
}
