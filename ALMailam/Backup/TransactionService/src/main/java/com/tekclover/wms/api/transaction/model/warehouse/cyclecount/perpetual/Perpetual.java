package com.tekclover.wms.api.transaction.model.warehouse.cyclecount.perpetual;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class Perpetual {

    @Valid
    private PerpetualHeaderV1 perpetualHeaderV1;

    @Valid
    private List<PerpetualLineV1> perpetualLineV1;

}
