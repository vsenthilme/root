package com.mnrclara.api.management.model.dto;

import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignmentLNEReport;
import com.mnrclara.api.management.model.mattergeneral.SearchMatterLNEReport;

import lombok.Data;

@Data
public class LNEMatter {

	private SearchMatterAssignmentLNEReport searchMatterAssignmentLNEReport;
	private SearchMatterLNEReport searchMatterLNEReport;
}
