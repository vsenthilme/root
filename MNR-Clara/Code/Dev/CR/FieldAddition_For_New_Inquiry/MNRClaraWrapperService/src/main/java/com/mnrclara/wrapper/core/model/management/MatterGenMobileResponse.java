package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

@Data
public class MatterGenMobileResponse {

    String matterNumber;
    String caseOpenedDate;
    String matterDescription;
    Long statusId;

}
