package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Referral { 
	
	private Long referralId;
	private String languageId;
	private Long classId;
	private Long subReferralId;
	private String referralDescription;
	private String subReferralDescription;
	private String referralStatus;
	private String createdBy;
    private Date createdOn;
    private String updatedBy;
	private Date updatedOn;
}