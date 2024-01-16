package com.mnrclara.api.cg.setup.model.store;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreIdCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `STATE_ID`,`COUNTRY_ID`, `LANG_ID`
     */
    private Long storeId;
    private String languageId;
    private String companyId;
}
