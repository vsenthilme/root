package com.tekclover.wms.api.masters.model.auditlog;

import lombok.Data;
import java.util.List;


@Data
public class SearchAuditLog {

    private List<String> languageId;

    private List<String> companyCodeId;

    private List<String> plantId;

    private List<String> warehouseId;

    private List<String> auditFileNumber;

    private List<Long> financialYear;
}
