package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Data
public class Group {

    @Valid
    private Set<ExactMatchGroup> exactMatchGroups;

    @Valid
    private Set<LikeMatchGroup> likeMatchGroup;

}
