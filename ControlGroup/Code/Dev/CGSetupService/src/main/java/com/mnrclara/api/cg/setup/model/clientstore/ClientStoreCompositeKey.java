package com.mnrclara.api.cg.setup.model.clientstore;

import lombok.Data;
import java.io.Serializable;

@Data
public class ClientStoreCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;
    /*
     * LANG_ID, COMPANY_ID, STORE_ID, CLIENT_ID, VERSION_NUMBER
     */
    private Long clientId;
    private Long storeId;
    private String companyId;
    private String languageId;
    private Long versionNumber;
}