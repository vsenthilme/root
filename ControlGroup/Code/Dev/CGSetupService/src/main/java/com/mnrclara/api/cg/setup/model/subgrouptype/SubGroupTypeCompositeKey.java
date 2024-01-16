package com.mnrclara.api.cg.setup.model.subgrouptype;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubGroupTypeCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `STATE_ID`,`GRP_TYP_ID`, `LANG_ID`,'SUB_GRP_TYP_ID'
     */

    private String companyId;
    private String languageId;
    private Long groupTypeId;
    private Long subGroupTypeId;
    private Long versionNumber;
}
