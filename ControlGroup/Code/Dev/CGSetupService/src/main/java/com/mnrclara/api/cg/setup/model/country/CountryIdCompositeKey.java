package com.mnrclara.api.cg.setup.model.country;

import lombok.Data;

import java.io.Serializable;

@Data
public class CountryIdCompositeKey implements Serializable {
    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `COUNTRY_ID`,`LANG_ID`,'C_ID',
     */
    private String countryId;
    private String languageId;
    private String companyId;
}
