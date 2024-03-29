package com.tekclover.wms.api.idmaster.model.levelid;
import lombok.Data;
import java.util.List;

@Data
public class FindLevelId {
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private List<Long> levelId;
    private String levelReference;
    private List<String>languageId;
}
