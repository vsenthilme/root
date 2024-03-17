package com.iweb2b.core.model.integration.asyad;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

/*
 *  {
        "consignments": [{
               "customer_code": "ASD",
               "reference_number": "",
               "service_type_id": "PREMIUM",
               "consignment_type": "forward",
               "load_type": "DOCUMENT",
               "description": "Sports Nutrition",
               "cod_favor_of": "",
               "cod_collection_mode": "dd",
               "dimension_unit": "cm",
               "length": "",
               "width": "",
               "height": "",
               "weight_unit": "kg",
               "weight": 2.63,
               "declared_value": 146,
               "cod_amount": "0.000",
               "num_pieces": 1,
               "notes": "Sports Nutrition",
               "customer_reference_number": "296163854",
               "is_risk_surcharge_applicable": false,
               "origin_details": {
                       "name": "The Hut Group",
                       "phone": "97101606811863",
                       "alternate_phone": "",
                       "address_line_1": "Mohebi Logistics; Plot WT01 & WT04",
                       "address_line_2": "LF Logistics/Mohebi Logistics, Plot",
                       "pincode": "8005",
                       "city": "Dubai",
                       "state": "",
                       "country": "AE"
               },

               "destination_details": {
                       "name": "Mohammad Alsmaim",
                       "phone": "96597990399",
                       "alternate_phone": "",
                       "address_line_1": "house 61 Block 5, street 517",
                       "address_line_2": "",
                       "pincode": "91710",
                       "city": "Jaber Alahmad",
                       "state": "",
                       "country": "KW"
               },

               "pieces_detail": [{
                       "description": "Sports Nutrition",
                       "declared_value": 0,
                       "weight": 2.63,
                       "height": 18,
                       "length": 11,
                       "width": 13
               }]
        }]
	}
 */
@Data
public class Consignment {

    private String customer_code;
    private String reference_number;
    private String service_type_id;
    private String consignment_type;
    private String load_type;
    private String description;
    private String cod_favor_of;
    private String cod_collection_mode;
    private String dimension_unit;
    private String length;
    private String width;
    private String height;
    private String weight_unit;
    private double weight;
    private double declared_value;
    private String cod_amount;
    private long num_pieces;
    private String notes;
    private String customer_reference_number;
    private Boolean is_risk_surcharge_applicable;

    private Date created_at;
    private String status_description;
    private String awb_3rd_Party;
    private String scanType;
    private String hubCode;

    private String orderType;
    private String jntPushStatus;
    private String boutiqaatPushStatus;
    
    private String customer_civil_id;
    private String receiver_civil_id;
    private String currency;
    private Boolean is_awb_printed;
    private String inco_terms;
    
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "softDataUploadId",fetch = FetchType.EAGER)
    private Origin_Details origin_details;
	
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "softDataUploadId",fetch = FetchType.EAGER)
    private Destination_Details destination_details;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "softDataUploadId",fetch = FetchType.EAGER)
    private Set<Pieces_Details> pieces_detail;

    private String hsn_code;
}
