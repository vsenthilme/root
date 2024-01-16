package com.iweb2b.api.integration.model.consignment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity (name = "JNTWebhookEntity")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbljntwebhook")
public class JNTWebhookEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JNT_WEBHOOK_ID")
    private Long jnt_webhook_id;
	
	@Column(name = "BILL_CODE", columnDefinition = "nvarchar(150)")
	private String billCode;
	
	@Column(name = "DESCRIPTION", columnDefinition = "nvarchar(255)")
	private String description;
	
	@Column(name = "SCAN_NETWORK_CITY", columnDefinition = "nvarchar(100)")
    private String scanNetworkCity;
	
	@Column(name = "SCAN_NETWORK_ID", columnDefinition = "nvarchar(100)")
    private Long scanNetworkId;
	
	@Column(name = "SCAN_NETWORK_NAME", columnDefinition = "nvarchar(255)")
    private String scanNetworkName;
	
	@Column(name = "SCAN_NETWORK_PROVINCE", columnDefinition = "nvarchar(100)")
    private String scanNetworkProvince;
	
	@Column(name = "SCAN_NETWORK_TYPE_NAME", columnDefinition = "nvarchar(100)")
    private String scanNetworkTypeName;
	
	@Column(name = "SCAN_TIME", columnDefinition = "nvarchar(100)")
    private String scanTime;
	
	@Column(name = "SCAN_TYPE", columnDefinition = "nvarchar(255)")
    private String scanType;
}