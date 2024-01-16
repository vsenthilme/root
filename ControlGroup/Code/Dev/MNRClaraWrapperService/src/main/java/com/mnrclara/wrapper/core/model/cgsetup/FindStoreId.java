package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindStoreId {

    private List<String> languageId;
    private List<String> companyId;
    private List<Long> storeId;
    private List<Long> groupTypeId;
    private List<Long> subGroupTypeId;
    private List<String> city;
    private List<String> state;
    private List<String> country;
    private List<String> groupTypeName;
    private List<String> subGroupTypeName;
    private List<Long> status;
    private String startCreatedOn;
    private String endCreatedOn;
}
