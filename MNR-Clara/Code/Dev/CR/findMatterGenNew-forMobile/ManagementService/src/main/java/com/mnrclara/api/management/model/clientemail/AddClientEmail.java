package com.mnrclara.api.management.model.clientemail;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddClientEmail {

	private Date emailDateTime = new Date();

	private Long classId;

	@NotBlank(message = "Client ID is mandatory")
	private String clientId;

	private String matterNumber;

	private String subject;

	private String cc;

	private String body;

	private Long deletionIndicator = 0L;

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
}
