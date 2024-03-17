package com.mnrclara.wrapper.core.model.management;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchQbSync {

	private String objectId;
	private Date startCreatedOn;
	private Date endCreatedOn;
}
