package com.tekclover.wms.core.model.user;

import java.util.Date;

import lombok.Data;

@Data
public class AddUserManagement {

    private String userId;
	private String languageId;
	private String companyCode;
	private String plantId;
	private String warehouseId;
	private Long userRoleId;
	private Long userTypeId;
    private String password;
    private String userName;
	private String companyIdAndDescription;
	private String plantIdAndDescription;
	private String warehouseIdAndDescription;
    private String firstName;
    private String lastName;
	private Long statusId;
   	private Long dateFormatId;
   	private Long currencyDecimal;
    private String timeZone;
    private String emailId;
	private Boolean isLoggedIn;
	private Boolean resetPassword;
	private Long deletionIndicator = 0L;
	private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
	private Date updatedOn = new Date();
}
