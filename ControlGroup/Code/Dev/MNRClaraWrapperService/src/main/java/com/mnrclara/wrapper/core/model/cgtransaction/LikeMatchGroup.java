package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import java.util.List;

@Data
public class LikeMatchGroup {

    private String groupName;

    private String groupId;

    private List<Stores> stores;
}
