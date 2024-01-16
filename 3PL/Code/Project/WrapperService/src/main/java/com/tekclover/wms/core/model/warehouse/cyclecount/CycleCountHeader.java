package com.tekclover.wms.core.model.warehouse.cyclecount;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "tblcyclecountheader")
public class CycleCountHeader {

    @Id
    @Column(name = "order_id",nullable = false)
    private String orderId;

    private String companyCode;
    private String cycleCountNo;
    private String branchCode;
    private String branchName;
    private Date cycleCountCreationDate;
    private String isNew;
    private Date orderProcessedOn;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "orderId",fetch = FetchType.EAGER)
    private Set<CycleCountLine> lines;

}
