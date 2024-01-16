package com.iwmvp.core.model.master;

import lombok.Data;
import java.util.Date;
@Data
public class NumberRange {
	private String languageId;
	private String companyId;
	private Long numberRangeCode;
	private String numberRangeObject;
	private Long numberRangeFrom;
	private Long numberRangeTo;
	private Long numberRangeCurrent;
	private Long numberRangeStatus;
	private Long deletionIndicator;
	private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
	private Date updatedOn = new Date();
}
