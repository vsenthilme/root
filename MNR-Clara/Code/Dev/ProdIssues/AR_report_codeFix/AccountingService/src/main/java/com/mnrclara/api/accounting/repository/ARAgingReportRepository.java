package com.mnrclara.api.accounting.repository;

import com.mnrclara.api.accounting.model.impl.ARAgingReportImpl;
import com.mnrclara.api.accounting.model.impl.ARReportImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.report.ARAgingReport;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ARAgingReportRepository extends JpaRepository<ARAgingReport, Long>, JpaSpecificationExecutor<ARAgingReport> {

    @Query(value = "select \n"
            + "	MATTER_NO matterNumber,\n"
            + "	CLASS_ID classId,\n"
            + "	CLIENT_ID clientId,\n"
            + "	TOTAL_AMT_DUE totalAmountDue,\n"
            + "	UNPAID_CURRENT unpaidCurrent,\n"
            + "	UNPAID_31_60_DAYS unpaid30To60Days,\n"
            + "	UNPAID_61_90_DAYS unpaid61To90Days,\n"
            + "	UNPAID_91_120_DAYS unpaid91DaysTo120Days,\n"
            + "	UNPAID_OVER120 unpaidOver120,\n"
            + "	BILLING_NOTES billingNotes,\n"
            + "	CLIENT_NAME clientName,\n"
            + "	ACC_PHONE_NO accountingPhoneNumber,\n"
            + "	CASE_CAT_ID caseCategoryId,\n"
            + "	CASE_SUB_CAT_ID caseSubCategoryId,\n"
            + "	MATTER_OPEN_DATE matterOpenDate,\n"
            + "	MATTER_NAME matterName,\n"
            + "	LAST_PMT_DATE lastPaymentDate,\n"
            + "	FEE_RECEIVED feeReceived,\n"
            + "	STATUS_ID statusId,\n"
            + "	PARTNER partner,\n"
            + "	ORIGINATING_TK originatingTimeKeeper,\n"
            + "	RESPONSIBLE_TK responsibleTimeKeeper,\n"
            + "	ASSIGNED_TK assignedTimeKeeper,\n"
            + "	LEGAL_ASSIST legalAssistant,\n"
            + "	PARALEGEL paralegal,\n"
            + "	LAW_CLERK lawClerk\n"
            + "	from tblaragingreport \n"
            + "	where \n"
            + "(COALESCE(:classId,null) IS NULL OR (CLASS_ID IN (:classId))) and \n"
            + "(COALESCE(:clientId,null) IS NULL OR (CLIENT_ID IN (:clientId))) and \n"
            + "(COALESCE(:matterNumber,null) IS NULL OR (MATTER_NO IN (:matterNumber))) and \n"
//            + "(COALESCE(:daysAging,null) IS NULL OR (UNPAID_91_120_DAYS IN (:daysAging))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (CASE_CAT_ID IN (:caseCategory))) and \n"
            + "(COALESCE(:statusId,null) IS NULL OR (STATUS_ID IN (:statusId))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (CASE_SUB_CAT_ID IN (:caseSubCategory))) and \n"
            + "(COALESCE(:timeKeepers,null) IS NULL OR PARTNER IN (:timeKeepers) OR ORIGINATING_TK IN (:timeKeepers) \n"
            + "OR RESPONSIBLE_TK IN (:timeKeepers) OR ASSIGNED_TK IN (:timeKeepers) OR LEGAL_ASSIST IN (:timeKeepers) \n"
            + "OR PARALEGEL IN (:timeKeepers) OR LAW_CLERK IN (:timeKeepers)) ", nativeQuery=true)
    public List<ARAgingReportImpl> getARAgingReport(
            @Param(value = "classId") Long classId,
            @Param(value = "clientId") List<String> clientId,
            @Param(value = "matterNumber") List<String> matterNumber,
//            @Param(value = "daysAging") List<String> daysAging,
            @Param(value = "caseCategory") List<Long> caseCategory,
            @Param(value = "caseSubCategory") List<Long> caseSubCategory,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "timeKeepers") List<String> timeKeepers);
	
}