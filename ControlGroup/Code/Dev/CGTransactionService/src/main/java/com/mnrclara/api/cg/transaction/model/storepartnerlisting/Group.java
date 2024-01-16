package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


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
