package com.mnrclara.api.management.model.mattergeneral;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AcademicReportInput {

    private List<Long> classId;
    private List<String> clientId;
    private List<String> corpClientId;
    private List<String> matterNumber;
    private List<String> documentType;
    private List<String> caseCategory;
    private List<String> caseSubCategory;
    private List<String> assignedTimeKeeper;
    private List<String> originatingTimeKeeper;
    private List<String> responsibleTimeKeeper;
    private List<String> partner;
    private List<String> legalAssistant;
    private List<String> paralegal;

    private String fromDate;
    private String toDate;

    private String fromFiledDate;
    private String toFiledDate;

    private Date startDate;
    private Date endDate;

    private Date caseFiledStartDate;
    private Date caseFiledEndDate;
}
