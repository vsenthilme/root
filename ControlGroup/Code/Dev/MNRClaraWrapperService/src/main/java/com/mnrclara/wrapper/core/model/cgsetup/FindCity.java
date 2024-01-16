package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindCity {
    private List<String> cityId;
    private List<String> cityName;
    private List<String> stateId;
    private List<String> countryId;
    private List<String> languageId;
    private List<String> companyId;
    private String startCreatedOn;
    private String endCreatedOn;
}
