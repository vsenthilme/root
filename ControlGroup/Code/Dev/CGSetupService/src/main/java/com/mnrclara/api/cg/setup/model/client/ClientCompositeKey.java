package com.mnrclara.api.cg.setup.model.client;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `C_ID`, `LANG_ID`
     */

    private String companyId;
    private String languageId;
    private Long clientId;
}
