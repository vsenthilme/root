package com.mnrclara.api.setup.model.userprofile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbluserotp")
@JsonInclude(Include.NON_NULL)
public class UserOTP {

	@Id
	@Column(name = "USR_ID")
	private String userId;
	
	@Column(name = "OTP_CODE")
	private Long otpCode;
	
	@Column(name = "OTP_SENT_TIME")
	private Date otpSentTime;
	
	@Column(name = "USR_LOG_TIME")
	private Date userLoggedInTime;
	
	@Column(name = "OTP_STATUS")
	private String status;
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
