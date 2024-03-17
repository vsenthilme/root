package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Data
public class ResponceObject {

    @Valid
    private Set<ExactMatchResultV2> exactMatchResult;

    @Valid
    private Set<LikeMatchResultV2> likeMatchResult;

}
