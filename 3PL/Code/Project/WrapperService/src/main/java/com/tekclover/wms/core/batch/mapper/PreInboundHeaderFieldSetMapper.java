package com.tekclover.wms.core.batch.mapper;

import com.tekclover.wms.core.batch.dto.PreInboundHeader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreInboundHeaderFieldSetMapper implements FieldSetMapper<PreInboundHeader> {


    @Override
    public PreInboundHeader mapFieldSet(FieldSet fieldSet){
        return new PreInboundHeader(
                fieldSet.readString("languageId"),
                fieldSet.readString("companyCode"),
                fieldSet.readString("plantId"),
                fieldSet.readString("warehouseId"),
                fieldSet.readString("preInboundNo"),
                fieldSet.readString("refDocNumber"),
                parseLong(fieldSet.readString("inboundOrderTypeId")),
                fieldSet.readString("referenceDocumentType"),
                parseLong(fieldSet.readString("statusId")),
                fieldSet.readString("containerNo"),
                parseLong(fieldSet.readString("noOfContainers")),
                fieldSet.readString("containerType"),
                parseDate(fieldSet.readString("refDocDate")),
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
                fieldSet.readString("companyDescription"),
                fieldSet.readString("plantDescription"),
                fieldSet.readString("warehouseDescription"),
                fieldSet.readString("statusDescription"));
    }

    //Long DataType
    private Long parseLong(String value){
        return value != null && !value.isEmpty() ? Long.parseLong(value) : null;
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
