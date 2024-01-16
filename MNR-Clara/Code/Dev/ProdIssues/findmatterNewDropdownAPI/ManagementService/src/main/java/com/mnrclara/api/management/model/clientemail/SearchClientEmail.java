package com.mnrclara.api.management.model.clientemail;

import java.util.Date;

import lombok.Data;

@Data
public class SearchClientEmail {
	/*
	 * MAIL_TYP, EMAIL_DATE_TIME, FROM, TO
	 */
	private String mailType;
	private Date startEmailDateTime = new Date();
	private Date endEmailDateTime = new Date();
	private String from;
	private String to;
}
