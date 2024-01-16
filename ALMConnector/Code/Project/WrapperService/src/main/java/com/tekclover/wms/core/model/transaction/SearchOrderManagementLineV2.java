package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchOrderManagementLineV2 extends SearchOrderManagementLine {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;

}