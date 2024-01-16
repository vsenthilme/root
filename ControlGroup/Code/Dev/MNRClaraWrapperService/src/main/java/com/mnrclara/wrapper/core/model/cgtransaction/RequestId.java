package com.mnrclara.wrapper.core.model.cgtransaction;

import lombok.Data;
import java.util.Date;

@Data
public class RequestId {

    private Long id;

    private Long requestId;

    private String fileName;

    private Long statusId;

    private Long deletionIndicator = 0L;

    private String referenceField1;

    private String referenceField2;

    private String referenceField3;

    private String referenceField4;

    private String referenceField5;

    private String referenceField6;

    private String referenceField7;

    private String referenceField8;

    private String referenceField9;

    private String referenceField10;

    private String uploadedBy;

    private String reUploadedBy;

    private Date uploadedOn;

    private Date reUploadedOn;
}
