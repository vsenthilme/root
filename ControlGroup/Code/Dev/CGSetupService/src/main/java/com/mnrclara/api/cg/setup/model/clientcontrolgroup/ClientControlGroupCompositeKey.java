package com.mnrclara.api.cg.setup.model.clientcontrolgroup;


import lombok.Data;

import java.io.Serializable;

@Data
public class ClientControlGroupCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `C_ID`, `LANG_ID`,'CLIENT_ID','GRP_TYP_ID','GRP_ID''VERSION_NO'
     */


    private String companyId;
    private String languageId;
    private Long clientId;
    private Long groupTypeId;
//    private Long subGroupTypeId;
    private Long versionNumber;
}
