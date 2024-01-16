package com.mnrclara.api.management.model.mattergeneral;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FeeShareAttorneyReport {

	 private String matterNumber;
	 private String timeKeeperCode;
	 private String feeSharingPercentage;
	 private Double feeSharingAmount;

}
