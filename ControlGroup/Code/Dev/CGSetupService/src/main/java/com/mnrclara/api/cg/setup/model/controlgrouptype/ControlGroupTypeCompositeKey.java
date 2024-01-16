package com.mnrclara.api.cg.setup.model.controlgrouptype;

import lombok.Data;

import java.io.Serializable;

@Data
public class ControlGroupTypeCompositeKey implements Serializable {
    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `GRP_TYP_ID`,'C_ID',`LANG_ID`,'VERSION_NO'
     */
    private Long groupTypeId;
    private String companyId;
    private String languageId;
    private Long versionNumber;
}
