package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ResponceObject {

    @Valid
    private List<ExactMatchResultV2> exactMatchResult;

    @Valid
    private List<LikeMatchResultV2> likeMatchResult;

}
