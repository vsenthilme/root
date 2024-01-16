package com.mnrclara.api.management.service;

import org.springframework.stereotype.Service;

import com.mnrclara.api.management.model.dto.Dashboard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {
	
	public Dashboard generateDashboard (Long classId, String period) {
		/*
		 * Dropdown options( current month, current quarter, previous month, previous quarter,Current Year,Previous year)
		 */
//		public enum period {
//			"CURRENT MONTH", 
//			"CURRENT QUARTER", 
//			"PREVIOUS MONTH", 
//			"PREVIOUS QUARTER",
//			"CURRENT YEAR",
//			"PREVIOUS YEAR"
//		}
		return null;
	}

	public Double getCurrentMonthInvoice() {
		return null;
	}
	
}
