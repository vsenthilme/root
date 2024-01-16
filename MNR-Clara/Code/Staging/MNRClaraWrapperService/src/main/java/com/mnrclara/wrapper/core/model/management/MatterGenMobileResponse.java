package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;

@Data
public class MatterGenMobileResponse {

    String matterNumber;
    String caseOpenedDate;
    Long statusId;

}
