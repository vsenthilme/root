package com.mnrclara.wrapper.core.model.cgtransaction;

import lombok.Data;

import java.util.List;


@Data
public class FindRequestId {

    private List<Long> id;

    private List<Long> requestId;

    private List<String> fileName;
}
