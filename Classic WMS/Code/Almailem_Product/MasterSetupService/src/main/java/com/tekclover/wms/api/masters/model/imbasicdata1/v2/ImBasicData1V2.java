package com.tekclover.wms.api.masters.model.imbasicdata1.v2;

import com.tekclover.wms.api.masters.model.imbasicdata1.ImBasicData1;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@ToString(callSuper = true)
public class ImBasicData1V2 extends ImBasicData1 {

    private String manufacturerName;
    private String manufacturerFullName;
    private String manufacturerCode;
    private String brand;
    private String supplierPartNumber;
    private String remarks;

    private String isNew;
    private String isUpdate;
    private String isCompleted;

    //MiddleWare Fields
    private Long middlewareId;
    private String middlewareTable;
}