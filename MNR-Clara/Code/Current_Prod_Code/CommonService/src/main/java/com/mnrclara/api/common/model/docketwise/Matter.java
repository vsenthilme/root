package com.mnrclara.api.common.model.docketwise;

import java.util.Date;
import java.util.List;

@lombok.Data
public class Matter {

	private Long id;
	
	// To create new Matter, below fields are mandated
	//----START------------------------
	private String number;
	private String title;
	private String description;
	private Long client_id;
	private List<Integer> user_ids;
	//----END------------------------
	
	private String attorney_id;
	private Date created_at;
	private Date updated_at;
	private boolean archived;
	private String discarded_at;
	private String settings;
	private Long firm_id;
	private String receipt_number;
	private String preference_category_id;
	private String priority_date;
	private String priority_date_status;
	private String invoice_id;
	private String created_by_migration;
}
