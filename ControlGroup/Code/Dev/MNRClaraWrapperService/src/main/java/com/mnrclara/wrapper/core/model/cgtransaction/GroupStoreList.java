package com.mnrclara.wrapper.core.model.cgtransaction;

import lombok.Data;

import java.util.List;

@Data
public class GroupStoreList {

    private List<GroupIdList> groupIdList;
    private List<StoreList> storeLists;
}
