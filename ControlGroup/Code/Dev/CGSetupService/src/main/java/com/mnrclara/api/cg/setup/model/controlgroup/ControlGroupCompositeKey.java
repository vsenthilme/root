package com.mnrclara.api.cg.setup.model.controlgroup;


import lombok.Data;

import java.io.Serializable;

@Data
public class ControlGroupCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `C_ID`, `LANG_ID`,'GROUP_ID,'GROUP_TYP_ID'
     */

    private String companyId;
    private String languageId;
    private Long groupId;
    private Long groupTypeId;
    private Long versionNumber;
}
