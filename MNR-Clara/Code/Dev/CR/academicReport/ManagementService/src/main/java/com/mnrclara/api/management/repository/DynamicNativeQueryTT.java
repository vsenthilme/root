package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.matterexpense.SearchMatterExpense;
import org.springframework.data.repository.query.Param;

import com.mnrclara.api.management.model.dto.IMatterExpenseCountAndSum;
import com.mnrclara.api.management.model.mattergeneral.WIPAgedPBReportInput;

public interface DynamicNativeQueryTT {

	//---------------Bill-By-Group----------------------------------------------------------
	public List<String[]> findCountAndSumOfTimeTicketsByGroup1 (List<String> matterNumber, Date startDate, Date feesCutoffDate, 
			List<String> originatingTimeKeeper, List<String> responsibleTimeKeeper, List<String> assignedTimeKeeper);
	
	public List<String[]> findExpenseByGroup1(List<String> matterNumber, Date startDate, Date feesCutoffDate, 
			List<String> originatingTimeKeeper, List<String> responsibleTimeKeeper, List<String> assignedTimeKeeper);

	public List<String[]> findMatterExpense (SearchMatterExpense searchMatterExpense);
	
}

