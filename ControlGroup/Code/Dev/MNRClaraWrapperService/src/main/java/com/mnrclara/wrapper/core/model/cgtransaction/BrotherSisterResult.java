package com.mnrclara.wrapper.core.model.cgtransaction;

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
