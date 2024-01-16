package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import java.util.List;

@Data
public class GroupStoreList {

    private List<GroupIdList> groupIdList;
    private List<StoreList> storeLists;
}
