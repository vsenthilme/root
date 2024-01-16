package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


import lombok.Data;

import java.util.List;

@Data
public class StoreList {

    private String groupName;
    private String groupId;
    private List<Store> stores;

}
