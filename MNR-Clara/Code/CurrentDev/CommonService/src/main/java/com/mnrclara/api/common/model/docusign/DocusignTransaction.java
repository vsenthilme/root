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
@Table(name = "tbldocusigntrans")
@JsonInclude(Include.NON_NULL)
public class DocusignTransaction {

	@Id	
	@Column(name = "ID")
    private Long id = 0L;
	
	@Column(name = "CLIENT_ID")
    private String clientId;
	
	@Column(name = "ACCOUNT_ID")
    private String accountId;
	
	@Column(name = "ENV_ID")
    private String envelopeId;
	
	@Column(name = "DOC_ID")
    private String documentId;
	
	@Column(name = "SIGNER_NAME")
    private String signerName;
	
	@Column(name = "SIGNER_EMAIL")
    private String signerEmail;
	
	@Column(name = "SENT_ON")
    private String sentOn;
	
	@Column(name = "STATUS")
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
