package com.iweb2b.api.integration.model.consignment.dto;

import lombok.Data;

@Data
public class PiecesDetails {
	
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

    private String description;
    private String declaredValue;
    private String weight;
    private String height;
    private String length;
    private String width;   
}
