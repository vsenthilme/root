package com.mnrclara.wrapper.core.model.cgtransaction;

import lombok.Data;

import java.util.List;

@Data
public class FindBSControllingInterest {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> validationId;
    private List<String> groupId;
    private List<String> subGroupId;
}
