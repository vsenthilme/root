package com.mnrclara.wrapper.core.model.crm;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {

    private Long id;
    private String topic;
    private String message;
    private String userId;
    private String userType;
    private Boolean status;
    private Boolean deletionIndicator;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}
