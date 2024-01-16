package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class Pieces_Details {
	
	/*
	 * 		{
               "description": "Sports Nutrition",
               "declared_value": 0,
               "weight": 2.63,
               "height": 18,
               "length": 11,
               "width": 13
          	}
	 */

	private Long width;
	private Long height;
	private Long length;
	private Double volume;
	private Double weight;
	private Long quantity;
 	private String description;
 	private String volume_unit;
 	private String weight_unit;
 	private String product_code;
    private String declared_value;
    private String dimension_unit;
    private Long denormalized_width;
    private Long denormalized_height;
    private Long denormalized_length;
    private Long denormalized_volume;
    private Double denormalized_weight;
    private String denormalized_volume_unit;
    private String denormalized_weight_unit;
    private String denormalized_dimension_unit;
}
