package com.iweb2b.api.portal.model.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tblcustomeraccesstoken")
public class CustomerAccessToken implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID")
    private Long tokenId = 0L;
    
    @Column(name="TOKEN")
    private String token;
    
    @Column(name="CUST_NAME")
	private String customerName;
    
}
