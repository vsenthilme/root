package com.mnrclara.api.cg.setup.model.cgentity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CgEntityCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;
    /*
    'ENTITY_ID', 'CLIENT_ID', 'C_ID', 'LANG_ID'
    */
    private Long entityId;
    private Long clientId;
    private String companyId;
    private String languageId;
}
