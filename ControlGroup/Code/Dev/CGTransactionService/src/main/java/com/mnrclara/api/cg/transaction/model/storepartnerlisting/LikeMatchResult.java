package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import java.util.List;

@Data
public class LikeMatchResult {

    private String coOwnerName;
    private List<StoreInfo> store;
}
