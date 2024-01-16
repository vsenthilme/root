package com.mnrclara.qb.ws.services.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblqbsync")
public class QBSync { 
	
	@Id
	@Column(name = "ID") 
	private String id;
	
	@Column(name = "OBJ_NAME") 
	private String objectName;
	
	@Column(name = "ERROR_DESC") 
	private String error;
	
	@Column(name = "STATUS") 
	private Long statusId;
	
	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
