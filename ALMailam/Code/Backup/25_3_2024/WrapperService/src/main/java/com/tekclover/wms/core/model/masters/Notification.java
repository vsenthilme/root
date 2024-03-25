package com.tekclover.wms.core.model.masters;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblnotification")
@Where(clause = "status='false'")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = 0L;

	@Column(name = "topic")
	private String topic;

	@Column(name = "message",columnDefinition = "text")
	private String message;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_type")
	private String userType;

	@Column(name = "status")
	private Boolean status = false;

	@Column(name = "IS_DELETED")
	private Boolean deletionIndicator = false;
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
