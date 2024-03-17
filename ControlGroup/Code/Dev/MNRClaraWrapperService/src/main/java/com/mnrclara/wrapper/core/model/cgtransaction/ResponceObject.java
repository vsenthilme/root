package com.mnrclara.wrapper.core.model.cgtransaction;


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
