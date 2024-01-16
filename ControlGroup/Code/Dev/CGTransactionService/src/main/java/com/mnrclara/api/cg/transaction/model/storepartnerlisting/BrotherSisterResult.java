package com.mnrclara.api.cg.transaction.model.storepartnerlisting;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class BrotherSisterResult {

    @Valid
    private List<ExactBrotherSisterResult> exactBrotherSisterResults;

    @Valid
    private List<LikeBrotherSisterResult> likeBrotherSisterResults;

}
