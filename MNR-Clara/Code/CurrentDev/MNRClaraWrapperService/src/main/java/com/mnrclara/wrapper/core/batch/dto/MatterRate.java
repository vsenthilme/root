package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterRate { 
	
	private String languageId;
	private Long classId;	
	private String clientId;
	private String matterNumber;
	private String caseInformationNo;
	private String timeKeeperCode;
	private Double defaultRatePerHour;
	private Double assignedRatePerHour;
	private String rateUnit;
	private Long statusId;
	private Long deletionIndicator = 0L;
	private String createdBy;
    private Date createdOn = new Date();
	
    /**
     * 
     * @param languageId
     * @param classId
     * @param clientId
     * @param matterNumber
     * @param caseInformationNo
     * @param timeKeeperCode
     * @param defaultRatePerHour
     * @param assignedRatePerHour
     * @param rateUnit
     * @param statusId
     * @param deletionIndicator
     * @param createdBy
     * @param createdOn
     */
	public MatterRate (String languageId, Long classId, String clientId, String matterNumber, String caseInformationNo, 
			String timeKeeperCode, Double defaultRatePerHour, Double assignedRatePerHour, String rateUnit, 
			Long statusId, Long deletionIndicator, String createdBy, Date createdOn) {
		this.matterNumber = matterNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.clientId = clientId;
		this.timeKeeperCode = timeKeeperCode;
		this.caseInformationNo = caseInformationNo;
		this.defaultRatePerHour = defaultRatePerHour;
		this.assignedRatePerHour = assignedRatePerHour;
		this.rateUnit = rateUnit;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
}
