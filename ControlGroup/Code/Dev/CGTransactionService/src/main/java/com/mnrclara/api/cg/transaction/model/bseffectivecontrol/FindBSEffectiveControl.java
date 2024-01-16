package com.mnrclara.api.cg.transaction.model.bseffectivecontrol;

import lombok.Data;

import java.util.List;

@Data
public class FindBSEffectiveControl {

    private List<String> companyId;
    private List<String> languageId;
    private List<Long> validationId;
    private List<Long> clientId;
    private List<String> groupId;
    private List<String> subGroupId;
}
