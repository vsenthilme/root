package com.mnrclara.api.management.model.qb;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchQbSync {
	private String objectId;
	private Date startCreatedOn;
	private Date endCreatedOn;
}
