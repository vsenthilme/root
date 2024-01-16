package com.mnrclara.api.management.model.dto;

import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignmentIMMReport;
import com.mnrclara.api.management.model.mattergeneral.SearchMatterIMMReport;

import lombok.Data;

@Data
public class ImmigrationMatter {

	private SearchMatterAssignmentIMMReport searchMatterAssignmentIMMReport;
	private SearchMatterIMMReport searchMatterIMMReport;
}
