package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import java.util.List;

@Data
public class ExactMatchGroup {

    private String groupName;

    private String groupId;

    private List<Stores> stores;
}
