package com.iweb2b.api.integration.model.consignment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbljntfailureresponse")
public class JNTFailureResponse implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESPONSE_ID")
    private Long responseId;
    
    @Column(name="IW_FAILURE_RESPONSE_CODE" )
    private String responseCode;
    
    @Column(name="IW_FAILURE_RESPONSE_DESCRIPTION", columnDefinition = "nvarchar(500)")
	private String responseDescription;

    @Column(name="JNT_FAILURE_RESPONSE_REASON", columnDefinition = "nvarchar(500)" )
    private String jntFailureResponseReason;

    @Column(name="JNT_FAILURE_RESPONSE_SUB_REASON", columnDefinition = "nvarchar(500)")
	private String jntFailureResponseSubReason;

}
