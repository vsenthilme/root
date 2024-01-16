package com.mnrclara.api.docusign.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbldocusign")
@JsonInclude(Include.NON_NULL)
public class Docusign {

	@Id
	@Column(name = "CLIENT_ID")
    private String clientId;
	
	@Column(name = "AUTH_TOKEN")
    private String authToken;
	
	@Column(name = "REFRESH_TOKEN")
    private String refreshToken;
	
	@Column(name = "RE_REFRESH_TOKEN")
    private String reRefreshToken;
	
	@Column(name = "CTD_BY")
    private String createdBy;
	
	@Column(name = "CTD_ON")
    private Date createdOn = new Date();
	
	@Column(name = "UTD_BY")
    private String updatedBy;
	
	@Column(name = "UTD_ON")
    private Date updatedOn = new Date();
}
