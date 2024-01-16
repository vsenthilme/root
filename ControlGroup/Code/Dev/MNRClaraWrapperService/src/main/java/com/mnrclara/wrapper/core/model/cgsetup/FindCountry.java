package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindCountry {
    private List<String> countryId;
    private List<String> countryName;
    private List<String> languageId;
    private List<String> companyId;
    private String startCreatedOn;
    private String endCreatedOn;

}
