package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import java.io.Serializable;

@Data
public class StorePartnerListingCompositeKey implements Serializable {
    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `REQUEST_ID`,`LANG_ID`,'C_ID',
     */

    private String languageId;
    private String companyId;
    private String storeId;
    private Long versionNumber;
}
