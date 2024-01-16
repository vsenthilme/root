package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindLanguageId {
    private List<String> languageId;
    private String startCreatedOn;
    private String endCreatedOn;
}
