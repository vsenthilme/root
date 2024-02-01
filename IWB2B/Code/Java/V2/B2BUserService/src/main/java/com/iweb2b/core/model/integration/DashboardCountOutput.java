package com.iweb2b.core.model.integration;

import lombok.Data;

@Data
public class DashboardCountOutput {

    private Long boutiqaatPassCount;
    private Long boutiqaatFailCount;
    private Long jntPassCount;
    private Long jntFailCount;
    private Long iwintlPassCount;
    private Long iwintlFailCount;
}