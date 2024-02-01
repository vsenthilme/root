package com.iweb2b.api.integration.model.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name = "tbluseraccess",
		uniqueConstraints = {
				@UniqueConstraint (
						name = "unique_key_useraccess",
						columnNames = {"LANG_ID", "C_ID", "USR_ID"}
						)
				}
		)
@IdClass(UserAccessCompositeKey.class)
public class UserAccess {

    @Id
    @Column(name = "USR_ID",columnDefinition = "nvarchar(100)")
    private String userId;
    
    @Id
    @Column(name = "LANG_ID",columnDefinition = "nvarchar(50)")
	private String languageId;
    
    @Id
    @Column(name = "C_ID",columnDefinition = "nvarchar(50)")
	private String companyCode;

    @Column(name = "USR_ROLE_ID")
	private Long userRoleId;

	@Column(name = "USR_TYP_ID")
	private Long userTypeId;
	
    @Column(name = "PASSWORD",columnDefinition = "nvarchar(100)")
    private String password;
	
    @Column(name = "USER_NM",columnDefinition = "nvarchar(150)")
    private String userName;
    
    @Column(name = "FST_NM",columnDefinition = "nvarchar(100)")
    private String firstName;
    
    @Column(name = "LST_NM",columnDefinition = "nvarchar(100)")
    private String lastName;
    
    @Column(name = "STATUS_ID")
	private Long statusId;
    
    @Column(name = "DATE_FOR_ID")
   	private Long dateFormatId;
    
    @Column(name = "CUR_DECIMAL")
   	private Long currencyDecimal;
    
    @Column(name = "TIME_ZONE")
    private String timeZone;

    @Column(name="IS_LOGGED_IN")
    private Boolean isLoggedIn;

    @Column(name="RESET_PASSWORD")
    private Boolean resetPassword;
    
    @Column(name = "MAIL_ID",columnDefinition = "nvarchar(150)")
    private String emailId;
    
    @Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

	@Column(name = "CTD_BY",columnDefinition = "nvarchar(50)")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
