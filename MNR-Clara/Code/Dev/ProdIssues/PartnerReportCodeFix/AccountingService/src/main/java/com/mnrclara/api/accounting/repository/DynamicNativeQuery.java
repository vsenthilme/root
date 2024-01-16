package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

public interface DynamicNativeQuery {

	public List<String[]> getBilledIncome (Long classId, Date fromPostingDate, Date toPostingDate); 
	public List<String[]> getCaseAssignment (Long classId, String timeKeeper);
	public List<String[]> getBillableNonBillableTime (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getClientReferral (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getPracticeBreakDown (Long classId, Date fromPostingDate, Date toPostingDate);
	public List<String[]> getTopClients (Date fromPostingDate, Date toPostingDate);
	public List<String[]> getLeadConversion (Long classId, Date fromPostingDate, Date toPostingDate);
}
