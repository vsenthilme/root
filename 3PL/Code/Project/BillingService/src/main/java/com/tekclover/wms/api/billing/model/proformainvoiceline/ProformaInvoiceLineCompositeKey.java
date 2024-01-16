package com.tekclover.wms.api.billing.model.proformainvoiceline;


import lombok.Data;

import java.io.Serializable;

@Data
public class ProformaInvoiceLineCompositeKey implements Serializable {

    private static final long serialVersionUID = -7617672247680004647L;

    /*
     * `LANG_ID`,`C_ID`, `PLANT_ID`, `WH_ID`,`PROFORMA_BILL_NO`,`PARTNER_CODE`,'LINE_NO'
     */
    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private String proformaBillNo;
    private String partnerCode;
    private Long lineNumber;
}
