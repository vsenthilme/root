package com.iwmvp.core.model.master;

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

    private String description;
    private double declared_value;
    private double weight;
    private double height;
    private long length;
    private long width; 
}
