package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.accounting.model.prebill.BillByGroup;

public interface DynamicNativeQuery {

	public List<String[]> getBilledIncome (Long classId, Date fromPostingDate, Date toPostingDate); 
	public List<String[]> getCaseAssignment (Long classId, String timeKeeper);
	public List<String[]> getBillableNonBillableTime (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getClientReferral (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getPracticeBreakDown (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getTopClients (Date fromPostingDate, Date toPostingDate);
	public List<String[]> getLeadConversion (Long classId, Date fromPostingDate, Date toPostingDate);
	
	// Bill by Group
	public List<String> findMatterNumbersForBillGroup (BillByGroup billByGroup);
}
