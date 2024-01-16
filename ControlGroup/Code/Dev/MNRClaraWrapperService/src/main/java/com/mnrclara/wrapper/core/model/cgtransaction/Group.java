package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class Group {

    @Valid
    private List<LikeMatchGroup> likeMatchGroup;

    @Valid
    private List<ExactMatchGroup> exactMatchGroups;
}
