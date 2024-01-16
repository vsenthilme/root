package com.mnrclara.spark.core.service.wmscore;



import com.mnrclara.spark.core.model.wmscore.OrderManagementLine;
import com.mnrclara.spark.core.model.wmscore.SearchOrderManagementLine;
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
public class OrderManagementLineServiceV2 {
    Properties conProp = new Properties();

    SparkSession sparkSession = null;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public OrderManagementLineServiceV2() throws ParseException {
        //connection properties
        conProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conProp.put("user", "sa");
        conProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("OrderManagementLine.com") .config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read().option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblordermangementline", conProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblordermangementlinev3");
    }


    /**
     *
     * @param searchOrderManagementLine
     * @return
     * @throws ParseException
     */
    public List<OrderManagementLine> findOrderManagementLineV3(SearchOrderManagementLine searchOrderManagementLine)
            throws ParseException {

        Dataset<Row> orderManagementLineQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_OB_NO as preOutboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "OB_LINE_NO as lineNumber, "
                + "ITM_CODE as itemCode, "
                + "PROP_ST_BIN as proposedStorageBin, "
                + "PROP_PACK_BARCODE as proposedPackBarCode, "
                + "PU_NO as pickupNumber, "
                + "VAR_ID as variantCode, "
                + "VAR_SUB_ID as variantSubCode, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "STATUS_ID as statusId, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "ITEM_TEXT as description, "
                + "MFR_PART as manufacturerPartNo, "
                + "HSN_CODE as hsnCode, "
                + "ITM_BARCODE as itemBarcode, "
                + "ORD_QTY as orderQty, "
                + "ORD_UOM as orderUom, "
                + "INV_QTY as inventoryQty, "
                + "ALLOC_QTY as allocatedQty, "
                + "RE_ALLOC_QTY as reAllocatedQty, "
                + "STR_TYP_ID as strategyTypeId, "
                + "ST_NO as strategyNo, "
                + "REQ_DEL_DATE as requiredDeliveryDate, "
                + "PROP_STR_NO as proposedBatchSerialNumber, "
                + "PROP_PAL_CODE as proposedPalletCode, "
                + "PROP_CASE_CODE as proposedCaseCode, "
                + "PROP_HE_NO as proposedHeNo, "
                + "PROP_PICKER_ID as proposedPicker, "
                + "ASS_PICKER_ID as assignedPickerId, "
                + "REASS_PICKER_ID as reassignedPickerId, "
                + "IS_DELETED as deletionIndicator, "
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
                + "RE_ALLOC_BY as reAllocatedBy, "
                + "RE_ALLOC_ON as reAllocatedOn, "
                + "PICK_UP_CTD_BY as pickupCreatedBy, "
                + "PICK_UP_CTD_ON as pickupCreatedOn, "
                + "PICK_UP_UTD_BY as pickupUpdatedBy, "
                + "PICK_UP_UTD_ON as pickupUpdatedOn, "
                + "PICKER_ASSIGN_BY as pickerAssignedBy, "
                + "PICKER_ASSIGN_ON as pickerAssignedOn, "
                + "PICKER_REASSIGN_BY as pickerReassignedBy, "
                + "PICKER_REASSIGN_ON as pickerReassignedOn "
                + "FROM tblordermangementlinev3 WHERE IS_DELETED = 0");

        orderManagementLineQueryV3.cache();

        if (searchOrderManagementLine.getWarehouseId() != null && !searchOrderManagementLine.getWarehouseId().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("WH_ID").isin(searchOrderManagementLine.getWarehouseId().toArray()));
        }
        if (searchOrderManagementLine.getPreOutboundNo() != null && !searchOrderManagementLine.getPreOutboundNo().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("PRE_OB_NO").isin(searchOrderManagementLine.getPreOutboundNo().toArray()));
        }
        if (searchOrderManagementLine.getRefDocNumber() != null && !searchOrderManagementLine.getRefDocNumber().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REF_DOC_NO").isin(searchOrderManagementLine.getRefDocNumber().toArray()));
        }
        if (searchOrderManagementLine.getPartnerCode() != null && !searchOrderManagementLine.getPartnerCode().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("PARTNER_CODE").isin(searchOrderManagementLine.getPartnerCode().toArray()));
        }
        if (searchOrderManagementLine.getItemCode() != null && !searchOrderManagementLine.getItemCode().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("ITM_CODE").isin(searchOrderManagementLine.getItemCode().toArray()));
        }
        if (searchOrderManagementLine.getStatusId() != null && !searchOrderManagementLine.getStatusId().isEmpty()) {
            List<String> statusIdStrings = searchOrderManagementLine.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("STATUS_ID").isin(statusIdStrings.toArray()));
        }

        if (searchOrderManagementLine.getOutboundOrderTypeId() != null && !searchOrderManagementLine.getOutboundOrderTypeId().isEmpty()) {
            List<String> outboundOrderTypeIdStrings = searchOrderManagementLine.getOutboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("OB_ORD_TYP_ID").isin(outboundOrderTypeIdStrings.toArray()));
        }
        if (searchOrderManagementLine.getDescription() != null && !searchOrderManagementLine.getDescription().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("ITEM_TEXT").isin(searchOrderManagementLine.getDescription().toArray()));
        }
        if (searchOrderManagementLine.getSoType() != null && !searchOrderManagementLine.getSoType().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REF_FIELD_1").isin(searchOrderManagementLine.getSoType().toArray()));
        }
        if (searchOrderManagementLine.getStartRequiredDeliveryDate() != null) {
            Date startDate = searchOrderManagementLine.getStartRequiredDeliveryDate();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REQ_DEL_DATE").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOrderManagementLine.getEndRequiredDeliveryDate() != null) {
            Date endDate = searchOrderManagementLine.getEndRequiredDeliveryDate();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REQ_DEL_DATE").$less$eq(dateFormat.format(endDate)));
        }
        if (searchOrderManagementLine.getStartOrderDate() != null) {
            Date startDate = searchOrderManagementLine.getStartOrderDate();
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REQ_DEL_DATE").$greater$eq(dateFormat.format(startDate)));
        }
        if (searchOrderManagementLine.getEndOrderDate() != null) {
            Date endDate = searchOrderManagementLine.getEndOrderDate();
            endDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("REQ_DEL_DATE").$less$eq(dateFormat.format(endDate)));
        }

        if (searchOrderManagementLine.getLanguageId() != null && !searchOrderManagementLine.getLanguageId().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("LANG_ID").isin(searchOrderManagementLine.getLanguageId().toArray()));
        }
        if (searchOrderManagementLine.getCompanyCodeId() != null && !searchOrderManagementLine.getCompanyCodeId().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("C_ID").isin(searchOrderManagementLine.getCompanyCodeId().toArray()));
        }
        if (searchOrderManagementLine.getPlantId() != null && !searchOrderManagementLine.getPlantId().isEmpty()) {
            orderManagementLineQueryV3 = orderManagementLineQueryV3.filter(col("PLANT_ID").isin(searchOrderManagementLine.getPlantId().toArray()));
        }


        Encoder<OrderManagementLine> OrderManagementLineEncoder = Encoders.bean(OrderManagementLine.class);
        Dataset<OrderManagementLine> dataSetControlGroup = orderManagementLineQueryV3.as(OrderManagementLineEncoder);
        List<OrderManagementLine> result = dataSetControlGroup.collectAsList();

        return result;
    }

}
