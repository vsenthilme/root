package com.mnrclara.api.management.model.mattertask;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
public class NotificationSave {

	private String topic;
	private String message;
	private List<String> userId = new ArrayList<>();
	private String userType;
	private Date createdOn;
	private String createdBy;
}
