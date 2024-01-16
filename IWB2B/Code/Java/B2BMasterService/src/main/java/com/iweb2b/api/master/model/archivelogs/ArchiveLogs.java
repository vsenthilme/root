package com.iweb2b.api.master.model.archivelogs;

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
@Table(name = "tblarchivelogs")
public class ArchiveLogs {

	@Id
	@Column(name = "PARTNER_ID")
	private String partnerId;

	@Column(name = "PARTNER_NAME")
	private String partnerName;
	
	@Column(name = "ACCESS_LOG_USER")
	private String accessLogUser;
		
	@Column(name = "ACCESS_LOG_API")
	private String accessLogApi;
		
	@Column(name = "AVERAGE_RESPONSE_TIME")
	private Date averageResponseTime;
		
	@Column(name = "RESPONSE_TIME_GREATERTHAN_AVERAGE")
	private String responseTimeGreaterthanAverage;
	
	@Column(name = "DELIVERY_FREQUENCY")
	private String deliveryFrequency;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

	@Column(name = "REF_FIELD_1")
	private String referenceField1;

	@Column(name = "REF_FIELD_2")
	private String referenceField2;

	@Column(name = "REF_FIELD_3")
	private String referenceField3;

	@Column(name = "REF_FIELD_4")
	private String referenceField4;

	@Column(name = "REF_FIELD_5")
	private String referenceField5;

	@Column(name = "REF_FIELD_6")
	private String referenceField6;

	@Column(name = "REF_FIELD_7")
	private String referenceField7;

	@Column(name = "REF_FIELD_8")
	private String referenceField8;

	@Column(name = "REF_FIELD_9")
	private String referenceField9;

	@Column(name = "REF_FIELD_10")
	private String referenceField10;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
	private Date createdOn;

	@Column(name = "UTD_BY")
	private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
