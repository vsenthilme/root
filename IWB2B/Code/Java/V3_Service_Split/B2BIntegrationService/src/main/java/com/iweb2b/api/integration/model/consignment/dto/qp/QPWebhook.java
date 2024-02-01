package com.iweb2b.api.integration.model.consignment.dto.qp;

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
@Entity (name = "QPWebhook")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblqpwebhook")
public class QPWebhook {
	/*
	 *  "tracking_No": "QP12333536TM",
        "item_Action_Id": 5,
        "item_Action_Name": "CREATED",
        "action_Date": "2023-06-12T12:56:33.317",
        "action_Time": "2023-06-12T12:56:33.317",
        "operator_Name": "0",
        "totalRows": 1
        
        
        QPData(
        tracking_No=BOQ10023, item_Action_Id=6, 
        item_Action_Name=DELIVERED, 
        action_Date=2023-12-25T10:31:39.323, 
        action_Time=2023-12-25T10:31:39.323, 
        operator_Name=Test_User, totalRows=5)
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QP_HOOK_ID")
    private Long qpHookID = 1L;
    
    @Column(name = "TRACKING_NO")
	private String trackingNo;
    
    @Column(name = "ITEM_ACTION_ID")
	private Long itemActionId;
    
    @Column(name = "ITEM_ACTION_NAME")
	private String itemActionName;
    
    @Column(name = "ACTION_DATE")
	private String actionDate;
    
    @Column(name = "ACTION_TIME")
	private String actionTime;
    
    @Column(name = "OPERATOR_NAME")
	private String operatorName;
    
    @Column(name = "TOTALROWS")
	private Long totalRows;
}