package com.iweb2b.api.integration.model.consignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbldestinationdetail")
public class DestinationDetailEntity {

	@Id
    @Column(name = "DESTINATION_ID")
    private Long destinationId = 0L;
	
    @Column(name = "CONSIGNMENT_ID")
    private Long consignmentId = 0L;

	@Column(name = "ADDRESS_HUB_CODE", columnDefinition = "nvarchar(100)")
	private String address_hub_code;

	@Column(name = "NAME", columnDefinition = "nvarchar(255)")
	private String name;

	@Column(name = "PHONE", columnDefinition = "nvarchar(100)")
	private String phone;

	@Column(name = "ALTERNATE_PHONE", columnDefinition = "nvarchar(100)")
	private String alternate_phone;

	@Column(name = "ADDRESS_LINE_1", columnDefinition = "nvarchar(500)")
	private String address_line_1;

	@Column(name = "ADDRESS_LINE_2", columnDefinition = "nvarchar(500)")
	private String address_line_2;

	@Column(name = "PINCODE", columnDefinition = "nvarchar(50)")
	private String pincode;

	@Column(name = "DISTRICT", columnDefinition = "nvarchar(100)")
	private String district;

	@Column(name = "CITY", columnDefinition = "nvarchar(100)")
	private String city;

	@Column(name = "STATE", columnDefinition = "nvarchar(100)")
	private String state;

	@Column(name = "COUNTRY", columnDefinition = "nvarchar(100)")
	private String country;

	@Column(name = "LATITUDE", columnDefinition = "nvarchar(100)")
	private String latitude;

	@Column(name = "LONGITUDE", columnDefinition = "nvarchar(100)")
	private String longitude;
}