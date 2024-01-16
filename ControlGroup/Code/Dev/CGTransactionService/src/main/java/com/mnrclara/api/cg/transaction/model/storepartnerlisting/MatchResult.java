package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class MatchResult {

    @Valid
    private List<ExactMatchResult> exactMatchResult;

    @Valid
    private List<LikeMatchResult> likeMatchResult;

}
