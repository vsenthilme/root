package com.mnrclara.api.management.model.dto;

import lombok.Data;

@Data
public class AddTimekeeperCode {

    private String languageId;
	private Long classId;
	private String timekeeperCode;
	private Long userTypeId;
	private Double defaultRate;
	private String timekeeperName;
	private String rateUnit;
	private String timekeeperStatus;
}
