package com.iweb2b.core.model.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
public class CustomerAccessToken implements Serializable {
	
    private Long tokenId = 0L;
    private String token;
	private String customerName;
    
}
