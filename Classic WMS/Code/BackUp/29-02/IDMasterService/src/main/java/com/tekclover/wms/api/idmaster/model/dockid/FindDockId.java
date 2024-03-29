package com.tekclover.wms.api.idmaster.model.dockid;
import lombok.Data;
import java.util.List;

@Data
public class FindDockId {
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private List<String> dockId;
    private List<String> languageId;
}
