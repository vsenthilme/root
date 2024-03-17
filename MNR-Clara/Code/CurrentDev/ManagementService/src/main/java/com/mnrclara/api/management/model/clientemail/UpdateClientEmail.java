package com.mnrclara.api.management.model.clientemail;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateClientEmail {

	private Date emailDateTime = new Date();

	private String languageId;

	private Long classId;

	private String clientId;

	private String matterNumber;

	private String mailType;

	private String fromMailId;

	private String toMailId;

	private String subject;

	private String cc;

	private String bcc;

	private String body;

	private Long deletionIndicator;

	private String referenceField1;

	private String referenceField2;

	private String referenceField3;

	private String referenceField4;

	private String referenceField5;

	private String referenceField6;

	private String referenceField7;

	private String referenceField8;

	private String referenceField9;

	private String referenceField10;

	private String updatedBy;
}
