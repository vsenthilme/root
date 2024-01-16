package com.mnrclara.api.cg.setup.model.relationshipid;

import lombok.Data;

import java.io.Serializable;

@Data
public class RelationShipIdCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `R_SHIP_ID`,`LANG_ID`,'C_ID',
     */
    private Long relationShipId;
    private String languageId;
    private String companyId;
}
