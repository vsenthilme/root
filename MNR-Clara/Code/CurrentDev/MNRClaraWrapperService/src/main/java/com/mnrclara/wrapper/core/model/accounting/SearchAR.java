package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchAR {

	private List<Long> classId;
	private List<Long> caseCategory;
	private List<Long> caseSubCategory;
	private List<String> timeKeeper;
	private String fromDate;
	private String toDate;
	private Boolean includeClosedMatter = false;
	private List<String> clientId;
}
