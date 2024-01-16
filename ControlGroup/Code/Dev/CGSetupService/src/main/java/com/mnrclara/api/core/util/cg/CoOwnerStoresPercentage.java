package com.mnrclara.api.core.util.cg;


import java.util.List;

import lombok.Data;

@Data
public class CoOwnerStoresPercentage {

    private String coOwnerName;
    private List<StorePerc> storesPerc;
}
