package com.mnrclara.api.cg.setup.model.languageid;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindLanguageId {
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
