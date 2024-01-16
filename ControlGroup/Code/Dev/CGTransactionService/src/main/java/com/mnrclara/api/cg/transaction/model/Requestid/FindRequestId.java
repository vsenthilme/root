package com.mnrclara.api.cg.transaction.model.Requestid;

import lombok.Data;

import java.util.List;


@Data
public class FindRequestId {

    private List<Long> id;

    private List<Long> requestId;

    private List<String> fileName;
}
