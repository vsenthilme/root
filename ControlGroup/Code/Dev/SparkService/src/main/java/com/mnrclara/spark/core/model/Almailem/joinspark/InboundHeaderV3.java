package com.mnrclara.spark.core.model.Almailem.joinspark;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class InboundHeaderV3 {

    private String lang_id;
    private String c_id;
    private String plant_id;
    private String wh_id;
    private String ref_doc_no;
    private String pre_ib_no;
    private Long status_id;
    private Long ib_ord_typ_id;
    private String cont_no;
    private String veh_no;
    private String ib_text;
    private String is_deleted;
    private String ref_field_1;
    private String ref_field_2;
    private String ref_field_3;
    private String ref_field_4;
    private String ref_field_5;
    private String ref_field_6;
    private String ref_field_7;
    private String ref_field_8;
    private String ref_field_9;
    private String ref_field_10;
    private String ctd_by;
    private Timestamp ctd_on;
    private String utd_by;
    private Timestamp utd_on;
    private String ib_cnf_by;
    private Timestamp ib_cnf_on;
    private String c_text;
    private String plant_text;
    private String wh_text;
    private String status_text;
    private String purchase_order_number;
    private String middleware_id;
    private String middleware_table;
    private String manufacturer_full_name;
    private String ref_doc_type;
    private Long count_of_ord_lines;
    private Long received_lines;
    private Timestamp transfer_order_date;
    private String is_cancelled;
    private Timestamp m_updated_on;
    private String source_branch_code;
    private String source_company_code;
    private Double totalOrderQty;
    private Double totalAcceptedQty;
    private Double totalDamageQty;



}
