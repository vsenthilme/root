package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import java.util.List;

@Data
public class StoreList {

    private String groupName;
    private String groupId;
    private List<Store> stores;

}
