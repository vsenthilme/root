package com.tekclover.wms.core.model.warehouse.cyclecount;


import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "cyclecountline")
public class CycleCountLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cycleCountNo;
    private Long lineOfEachItemCode;
    private String itemCode;
    private String itemDescription;
    private String Uom;
    private String manufacturerCode;
    private String manufacturerName;
    private String orderId;
}
