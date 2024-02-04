package com.tekclover.wms.core.batch.mapper;


import com.tekclover.wms.core.batch.dto.PreInboundLine;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreInboundLineFieldSetMapper implements FieldSetMapper<PreInboundLine> {

    @Override
    public PreInboundLine mapFieldSet(FieldSet fieldSet){
        return new PreInboundLine(
                fieldSet.readString("languageId"),
                fieldSet.readString("companyCode"),
                fieldSet.readString("plantId"),
                fieldSet.readString("warehouseId"),
                fieldSet.readString("preInboundNo"),
                fieldSet.readString("refDocNumber"),
                parseLong(fieldSet.readString("lineNo")),
                fieldSet.readString("itemCode"),
                parseLong(fieldSet.readString("inboundOrderTypeId")),
                parseLong(fieldSet.readString("variantCode")),
                fieldSet.readString("variantSubCode"),
                parseLong(fieldSet.readString("statusId")),
                fieldSet.readString("itemDescription"),
                fieldSet.readString("containerNo"),
                fieldSet.readString("invoiceNo"),
                fieldSet.readString("businessPartnerCode"),
                fieldSet.readString("partnerItemNo"),
                fieldSet.readString("brandName"),
                fieldSet.readString("manufacturerPartNo"),
                fieldSet.readString("hsnCode"),
                parseDate(fieldSet.readString("expectedArrivalDate")),
                parseDouble(fieldSet.readString("orderQty")),
                fieldSet.readString("orderUom"),
                parseLong(fieldSet.readString("stockTypeId")),
                parseLong(fieldSet.readString("specialStockIndicatorId")),
                fieldSet.readString("numberOfPallets"),
                fieldSet.readString("numberOfCases"),
                parseDouble(fieldSet.readString("itemPerPalletQty")),
                parseDouble(fieldSet.readString("itemCaseQty")),
                fieldSet.readString("referenceField1"),
                fieldSet.readString("referenceField2"),
                fieldSet.readString("referenceField3"),
                fieldSet.readString("referenceField4"),
                fieldSet.readString("referenceField5"),
                fieldSet.readString("referenceField6"),
                fieldSet.readString("referenceField7"),
                fieldSet.readString("referenceField8"),
                fieldSet.readString("referenceField9"),
                fieldSet.readString("referenceField10"),
                fieldSet.readString("referenceField11"),
                fieldSet.readString("referenceField12"),
                fieldSet.readString("referenceField13"),
                fieldSet.readString("referenceField14"),
                fieldSet.readString("referenceField15"),
                fieldSet.readString("referenceField16"),
                fieldSet.readString("referenceField17"),
                fieldSet.readString("referenceField18"),
                fieldSet.readString("referenceField19"),
                fieldSet.readString("referenceField20"),
                parseLong(fieldSet.readString("deletionIndicator")),
                fieldSet.readString("createdBy"),
                fieldSet.readString("dType"),
                fieldSet.readString("manufacturerCode"),
                fieldSet.readString("manufacturerName"),
                fieldSet.readString("origin"),
                fieldSet.readString("companyDescription"),
                fieldSet.readString("plantDescription"),
                fieldSet.readString("warehouseDescription"),
                fieldSet.readString("statusDescription"));
    }

    //Long DataType
    private Long parseLong(String value){
        return value != null && !value.isEmpty() ? Long.parseLong(value) : null;
    }

    //Double DataType
    private Double parseDouble(String value){
        return value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
    }

    //Date Format
    private Date parseDate(String value){
        if(value == null || value.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(value);
        } catch (ParseException e){
            throw new IllegalArgumentException("Error parsing date: " + value, e);
        }
    }
}
