package com.iweb2b.core.model.integration;

import lombok.Data;

import java.util.Date;

@Data
public class PiecesDetail {

    private String description;
    private String declared_value;
    private String volume;
    private String weight;
    private String height;
    private String length;
    private String width;
    private String weight_unit;
    private String dimension_unit;
    private String volume_unit;
    private String piece_product_code;
    private Long chargeable_weight;
    private Long volumetric_weight;
    private Long denormalized_width;
    private Long denormalized_height;
    private Long denormalized_length;
    private Long denormalized_volume;
    private Double denormalized_weight;
    private String denormalized_volume_unit;
    private String denormalized_weight_unit;
    private String denormalized_dimension_unit;
}
