package com.mnrclara.api.cg.setup.model.store;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class FindStoreId {

    private List<String> languageId;
    private List<String> companyId;
    private List<Long> storeId;
    private List<String> city;
    private List<String> state;
    private List<Long> groupTypeId;
    private List<Long> subGroupTypeId;
    private List<String> groupTypeName;
    private List<String> subGroupTypeName;
    private List<String> country;
    private List<Long> status;
    private String startCreatedOn;
    private String endCreatedOn;
    private Date fromDate;
    private Date toDate;
}
