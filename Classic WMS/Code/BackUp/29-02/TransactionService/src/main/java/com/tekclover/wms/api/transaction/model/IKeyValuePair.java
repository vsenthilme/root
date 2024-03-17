package com.tekclover.wms.api.transaction.model;


public interface IKeyValuePair {

    String getCompanyDesc();

    String getPlantDesc();

    String getWarehouseDesc();

    Double getInventoryQty();

    String getItemCode();

    String getManufacturerName();

    String getReferenceCycleCountNo();

    String getRefDocNumber();
    String getAssignPicker();
    Long getPickerCount();

}
