package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.PreOutboundLine;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreOutboundLineFieldSetMapper implements FieldSetMapper<PreOutboundLine> {

    @Override
    public PreOutboundLine mapFieldSet(FieldSet fieldSet) {
        return new PreOutboundLine(
                fieldSet.readString("languageId"),
                fieldSet.readString("companyCodeId"),
                fieldSet.readString("plantId"),
                fieldSet.readString("warehouseId"),
                fieldSet.readString("refDocNumber"),
                fieldSet.readString("preOutboundNo"),
                fieldSet.readString("partnerCode"),
                parseLong(fieldSet.readString("lineNumber")),
                fieldSet.readString("itemCode"),
                parseLong(fieldSet.readString("outboundOrderTypeId")),
                parseLong(fieldSet.readString("variantCode")),
                fieldSet.readString("variantSubCode"),
                parseLong(fieldSet.readString("statusId")),
                parseLong(fieldSet.readString("stockTypeId")),
                parseLong(fieldSet.readString("specialStockIndicatorId")),
                fieldSet.readString("description"),
                fieldSet.readString("manufacturerCode"),
                fieldSet.readString("hsnCode"),
                fieldSet.readString("itemBarcode"),
                parseDouble(fieldSet.readString("orderQty")),
                fieldSet.readString("orderUom"),
                parseDate(fieldSet.readString("requiredDeliveryDate")),
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
                parseLong(fieldSet.readString("deletionIndicator")),
                fieldSet.readString("createdBy"),
                fieldSet.readString("dType"),
                fieldSet.readString("manufacturerCode"),
                fieldSet.readString("manufacturerName"),
                fieldSet.readString("origin"),
                fieldSet.readString("brand"),
                fieldSet.readString("companyDescription"),
                fieldSet.readString("plantDescription"),
                fieldSet.readString("warehouseDescription"),
                fieldSet.readString("statusDescription"));
    }

    //Long DataType
    private Long parseLong(String value) {
        return value != null && !value.isEmpty() ? Long.parseLong(value) : null;
    }

    //Double DataType
    private Double parseDouble(String value){
        return value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
    }

    //Date Format
    private Date parseDate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Error parsing date: " + value, e);
        }
    }
}
