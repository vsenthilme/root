package com.iweb2b.core.model.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table (name = "tblpost")
@Entity
@Data
public class Post {
	
	@Id
    private long id;
    private String name;
    private double price;
    private double discount;
    
    public Post() {
	}
    
    public Post(String name) {
    	this.name = name;
	}
}