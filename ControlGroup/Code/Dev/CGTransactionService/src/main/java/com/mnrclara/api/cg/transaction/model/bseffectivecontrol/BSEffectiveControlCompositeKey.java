package com.mnrclara.api.cg.transaction.model.bseffectivecontrol;

import lombok.Data;

import java.io.Serializable;

@Data
public class BSEffectiveControlCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `VALIDATION_ID`,`LANG_ID`,'C_ID',
     */

    private String companyId;
    private String languageId;
    private Long validationId;

}
