package com.iweb2b.api.integration.model.usermanagement;

import lombok.Data;

import java.util.Date;

@Data
public class AddUserAccess {

    private String userId;
	private String languageId;
	private String companyCode;
	private Long userRoleId;
	private Long userTypeId;
    private String password;
    private String userName;
    private String firstName;
    private String lastName;
	private Long statusId;
	private Boolean isLoggedIn;
	private Boolean resetPassword;
   	private Long dateFormatId;
   	private Long currencyDecimal;
    private String timeZone;
    private String emailId;
	private Long deletionIndicator;
	private String createdBy;
    private Date createdOn;
    private String updatedBy;
	private Date updatedOn;
}
