package com.iweb2b.api.integration.model.consignment.dto;

import java.util.Date;

public interface ConsignmentImpl {

    Long getConsignmentId();
    String getReference_number();
    String getCod_amount();
    String getCod_collection_mode();
    String getCustomer_code();
    String getService_type_id();
    String getConsignment_type();
    String getLoad_type();
    String getDescription();
    String getCod_favor_of();
    String getDimension_unit();
    String getLength();
    String getWidth();
    String getHeight();
    String getWeight_unit();
    Double getWeight();
    Double getDeclared_value();
    Long getNum_pieces();
    String getNotes();
    String getCustomer_reference_number();
    Boolean getIs_risk_surcharge_applicable();
    Date getCreated_at();
    String getStatus_description();
    String getCustomer_civil_id();
    String getReceiver_civil_id();
    String getCurrency();
    String getAwb_3rd_Party();
    String getScanType_3rd_Party();
    String getHubCode_3rd_Party();
    String getOrderType();
    String getJntPushStatus();
    String getBoutiqaatPushStatus();
    Boolean getIs_awb_printed();
    String getScanType();
    String getHubCode();
}