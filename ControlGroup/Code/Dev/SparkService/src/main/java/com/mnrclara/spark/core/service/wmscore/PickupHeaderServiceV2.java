package com.mnrclara.spark.core.service.wmscore;


import com.mnrclara.spark.core.model.wmscore.PickupHeader;
import com.mnrclara.spark.core.model.wmscore.SearchPickupHeader;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Service
@Slf4j
public class PickupHeaderServiceV2 {

    Properties connProp = new Properties();
    SparkSession sparkSession = null;

    public PickupHeaderServiceV2() throws ParseException {
        //connection properties
        connProp.setProperty("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connProp.put("user", "sa");
        connProp.put("password", "30NcyBuK");
        sparkSession = SparkSession.builder().master("local[*]").appName("PickupHeader.com").config("spark.executor.memory", "4g")
                .config("spark.executor.cores", "4").getOrCreate();

        //Read from Sql Table
        val df2 = sparkSession.read()
                .option("fetchSize", "10000").jdbc("jdbc:sqlserver://35.154.84.178;databaseName=WMS", "tblpickupheader", connProp)
                .repartition(16);
        df2.createOrReplaceTempView("tblpickupheaderv3");
    }

    /**
     *
     * @param searchPickupHeader
     * @return
     * @throws ParseException
     */
    public List<PickupHeader> findPickupHeaderV3(SearchPickupHeader searchPickupHeader) throws ParseException {

        Dataset<Row> pickupHeaderQueryV3 = sparkSession.sql("SELECT "
                + "LANG_ID as languageId, "
                + "C_ID as companyCodeId, "
                + "PLANT_ID as plantId, "
                + "WH_ID as warehouseId, "
                + "PRE_OB_NO as preOutboundNo, "
                + "REF_DOC_NO as refDocNumber, "
                + "PARTNER_CODE as partnerCode, "
                + "PU_NO as pickupNumber, "
                + "OB_LINE_NO as lineNumber, "
                + "ITM_CODE as itemCode, "
                + "PROP_ST_BIN as proposedStorageBin, "
                + "PROP_PACK_BARCODE as proposedPackBarCode, "
                + "OB_ORD_TYP_ID as outboundOrderTypeId, "
                + "PICK_TO_QTY as pickToQty, "
                + "PICK_UOM as pickUom, "
                + "STCK_TYP_ID as stockTypeId, "
                + "SP_ST_IND_ID as specialStockIndicatorId, "
                + "MFR_PART as manufacturerPartNo, "
                + "STATUS_ID as statusId, "
                + "ASS_PICKER_ID as assignedPickerId, "
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
                + "PICK_CTD_BY as pickupCreatedBy, "
                + "PICK_CTD_ON as pickupCreatedOn, "
                + "PICK_CNF_BY as pickConfirmedBy, "
                + "PICK_CNF_ON as pickConfirmedOn, "
                + "PICK_UTD_BY as pickUpdatedBy, "
                + "PICK_UTD_ON as pickUpdatedOn, "
                + "PICK_REV_BY as pickupReversedBy, "
                + "PICK_REV_ON as pickupReversedOn "
                + "FROM tblpickupheaderv3 WHERE IS_DELETED = 0 ");

        pickupHeaderQueryV3.cache();

        if (searchPickupHeader.getWarehouseId() != null && !searchPickupHeader.getWarehouseId().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("WH_ID").isin(searchPickupHeader.getWarehouseId().toArray()));
        }
        if (searchPickupHeader.getRefDocNumber() != null && !searchPickupHeader.getRefDocNumber().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("REF_DOC_NO").isin(searchPickupHeader.getRefDocNumber().toArray()));
        }
        if (searchPickupHeader.getPartnerCode() != null && !searchPickupHeader.getPartnerCode().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("PARTNER_CODE").isin(searchPickupHeader.getPartnerCode().toArray()));
        }
        if (searchPickupHeader.getPickupNumber() != null && !searchPickupHeader.getPickupNumber().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("PU_NO").isin(searchPickupHeader.getPickupNumber().toArray()));
        }
        if (searchPickupHeader.getItemCode() != null && !searchPickupHeader.getItemCode().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("ITM_CODE").isin(searchPickupHeader.getItemCode().toArray()));
        }
        if (searchPickupHeader.getProposedStorageBin() != null && !searchPickupHeader.getProposedStorageBin().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("PROP_ST_BIN").isin(searchPickupHeader.getProposedStorageBin().toArray()));
        }
        if (searchPickupHeader.getProposedPackCode() != null && !searchPickupHeader.getProposedPackCode().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("PROP_PACK_BARCODE").isin(searchPickupHeader.getProposedPackCode().toArray()));
        }
        if (searchPickupHeader.getOutboundOrderTypeId() != null && !searchPickupHeader.getOutboundOrderTypeId().isEmpty()) {
            List<String> outboundStrings = searchPickupHeader.getOutboundOrderTypeId().stream().map(String::valueOf).collect(Collectors.toList());
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("OB_ORD_TYP_ID").isin(outboundStrings.toArray()));
        }
        if (searchPickupHeader.getStatusId() != null && !searchPickupHeader.getStatusId().isEmpty()) {
            List<String> statusString = searchPickupHeader.getStatusId().stream().map(String::valueOf).collect(Collectors.toList());
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("STATUS_ID").isin(statusString.toArray()));
        }
        if (searchPickupHeader.getSoType() != null && !searchPickupHeader.getSoType().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("REF_FIELD_1").isin(searchPickupHeader.getSoType()));
        }
        if (searchPickupHeader.getAssignedPickerId() != null && !searchPickupHeader.getAssignedPickerId().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("ASS_PICKER_ID").isin(searchPickupHeader.getAssignedPickerId()));
        }

        if (searchPickupHeader.getLanguageId() != null && !searchPickupHeader.getLanguageId().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("LANG_ID").isin(searchPickupHeader.getLanguageId().toArray()));
        }
        if (searchPickupHeader.getCompanyCodeId() != null && !searchPickupHeader.getCompanyCodeId().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("C_ID").isin(searchPickupHeader.getCompanyCodeId().toArray()));
        }
        if (searchPickupHeader.getPlantId() != null && !searchPickupHeader.getPlantId().isEmpty()) {
            pickupHeaderQueryV3 = pickupHeaderQueryV3.filter(col("PLANT_ID").isin(searchPickupHeader.getPlantId().toArray()));
        }

        Encoder<PickupHeader> pickupHeaderEncoder = Encoders.bean(PickupHeader.class);
        Dataset<PickupHeader> dataSetControlGroup = pickupHeaderQueryV3.as(pickupHeaderEncoder);
        List<PickupHeader> result = dataSetControlGroup.collectAsList();
        return result;
    }
}
