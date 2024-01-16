package com.mnrclara.wrapper.core.model.cgsetup;


import lombok.Data;

import java.util.List;

@Data
public class FindStatus {

    private List<Long> statusId;
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
}
