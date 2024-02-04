package com.tekclover.wms.core.batch.mapper;
import com.tekclover.wms.core.batch.dto.PreOutboundHeader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreOutboundHeaderFieldSetMapper implements FieldSetMapper<PreOutboundHeader> {


    @Override
    public PreOutboundHeader mapFieldSet(FieldSet fieldSet) {
        return new PreOutboundHeader(
                fieldSet.readString("languageId"),
                fieldSet.readString("companyCodeId"),
                fieldSet.readString("plantId"),
                fieldSet.readString("warehouseId"),
                fieldSet.readString("refDocNumber"),
                fieldSet.readString("preOutboundNo"),
                fieldSet.readString("partnerCode"),
                parseLong(fieldSet.readString("outboundOrderTypeId")),
                fieldSet.readString("referenceDocumentType"),
                parseLong(fieldSet.readString("statusId")),
                parseDate(fieldSet.readString("refDocDate")),
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
                fieldSet.readString("remarks"),
                fieldSet.readString("createdBy"),
                fieldSet.readString("dType"),
                fieldSet.readString("companyDescription"),
                fieldSet.readString("plantDescription"),
                fieldSet.readString("warehouseDescription"),
                fieldSet.readString("statusDescription"));


    }

    //Long DataType
    private Long parseLong(String value) {
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
