package com.mnrclara.api.common.model.docusign;

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
@Table(name = "tbldocusigntoken")
@JsonInclude(Include.NON_NULL)
public class DocusignToken {

	@Id	
	@Column(name = "ID")
    private int id;
	
	@Column(name = "CLIENT_ID")
    private String clientId;
	
	@Column(name = "AUTH_TOKEN")
    private String authToken;
	
	@Column(name = "REFRESH_TOKEN")
    private String refreshToken;
	
	@Column(name = "EXPIRES_IN")
    private Long expiresIn;
	
	@Column(name = "TOKEN_GEN_ON")
    private Date tokenGeneratedOn;
	
	@Column(name = "CTD_BY")
    private String createdBy;
	
	@Column(name = "CTD_ON")
    private Date createdOn = new Date();
	
	@Column(name = "UTD_BY")
    private String updatedBy;
	
	@Column(name = "UTD_ON")
    private Date updatedOn = new Date();
}
