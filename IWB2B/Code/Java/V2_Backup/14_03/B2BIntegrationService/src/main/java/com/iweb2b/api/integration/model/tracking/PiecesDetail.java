package com.iweb2b.api.integration.model.tracking;

import lombok.Data;

@Data
public class PiecesDetail {
	
	/*
	 * "width": 5,
	    "height": 5,
	    "length": 5,
	    "volume": 427731.2,
	    "weight": 0.5,
	    "quantity": 1,
	    "description": "Notebook",
	    "volume_unit": "ccm",
	    "weight_unit": "kg",
	    "product_code": "000000000581109917",
	    "declared_value": "100",
	    "dimension_unit": "cm",
	    "denormalized_width": "5",
	    "denormalized_height": "5",
	    "denormalized_length": "5",
	    "denormalized_volume": "427731200.000",
	    "denormalized_weight": "0.5",
	    "denormalized_volume_unit": "mmq",
	    "denormalized_weight_unit": "kg",
	    "denormalized_dimension_unit": "cm"
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
