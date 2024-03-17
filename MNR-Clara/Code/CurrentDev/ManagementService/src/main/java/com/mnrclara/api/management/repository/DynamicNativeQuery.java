package com.mnrclara.api.management.repository;

import java.util.List;

import com.mnrclara.api.management.model.mattergeneral.WIPAgedPBReportInput;

public interface DynamicNativeQuery {

	public List<String[]> getMatterNumbers (WIPAgedPBReportInput wipAgedPBReportInput, int pageIndex, int noOfRecords); 
	public int getMatterNumbersCount (WIPAgedPBReportInput wipAgedPBReportInput); 
	public List<String[]> getMatterNumbers (WIPAgedPBReportInput wipAgedPBReportInput);
	public List<String[]> getMatterAssignment (List<String> partner, String matterNumber);
	public List<String[]> getMatterDetail (String matterNumber);
}

