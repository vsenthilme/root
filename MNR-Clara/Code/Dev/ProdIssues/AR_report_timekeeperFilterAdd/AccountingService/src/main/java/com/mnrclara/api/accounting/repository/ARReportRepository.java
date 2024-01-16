package com.mnrclara.api.accounting.repository;

import com.mnrclara.api.accounting.model.impl.ARReportImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.reports.ARReport;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ARReportRepository extends JpaRepository<ARReport, Long>, JpaSpecificationExecutor<ARReport> {

    @Query(value = "select \n"
            + "	CLASS_ID classId,\n"
            + "	CLIENT_ID clientId,\n"
            + "	CLIENT_NAME clientName,\n"
            + "	MATTER_NO matterNumber,\n"
            + "	CASE_CAT_ID caseCategory,\n"
            + "	CASE_SUB_CAT_ID caseSubCategory,\n"
            + "	MATTER_TEXT matterText,\n"
            + "	BILLING_FORMAT billingFormat,\n"
            + "	PHONE phone,\n"
            + "	FEES_DUE feesDue,\n"
            + "	HARD_COST_DUE hardCostsDue,\n"
            + "	SOFT_COST_DUE softCostsDue,\n"
            + "	TOTAL_DUE totalDue,\n"
            + "	POSTING_DATE postingDate,\n"
            + "	LAST_BILL_DATE lastBillDate,\n"
            + "	LAST_PAYMENT_DATE lastPaymentDate,\n"
            + "	PARTNER partner,\n"
            + "	ORIGINATING_TK originatingTimeKeeper,\n"
            + "	RESPONSIBLE_TK responsibleTimeKeeper,\n"
            + "	ASSIGNED_TK assignedTimeKeeper,\n"
            + "	LEGAL_ASSIST legalAssistant,\n"
            + "	PARALEGEL paralegal,\n"
            + "	LAW_CLERK lawClerk\n"
            + "	from tblarreport \n"
            + "	where \n"
            + "(COALESCE(:classId,null) IS NULL OR (CLASS_ID IN (:classId))) and \n"
            + "(COALESCE(:clientId,null) IS NULL OR (CLIENT_ID IN (:clientId))) and \n"
            + "(COALESCE(:matterNumber,null) IS NULL OR (MATTER_NO IN (:matterNumber))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (CASE_CAT_ID IN (:caseCategory))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (CASE_SUB_CAT_ID IN (:caseSubCategory))) and \n"
            + "(COALESCE(:fromDate,null) IS NULL OR (POSTING_DATE BETWEEN :fromDate AND :toDate)) and \n"
            + "(COALESCE(:timeKeeper,null) IS NULL OR PARTNER IN (:timeKeeper) OR ORIGINATING_TK IN (:timeKeeper) \n"
            + "OR RESPONSIBLE_TK IN (:timeKeeper) OR ASSIGNED_TK IN (:timeKeeper) OR LEGAL_ASSIST IN (:timeKeeper) \n"
            + "OR PARALEGEL IN (:timeKeeper) OR LAW_CLERK IN (:timeKeeper)) ", nativeQuery = true)
    public List<ARReportImpl> getARReport(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "clientId") List<String> clientId,
            @Param(value = "matterNumber") List<String> matterNumber,
            @Param(value = "caseCategory") List<Long> caseCategory,
            @Param(value = "caseSubCategory") List<Long> caseSubCategory,
            @Param(value = "timeKeeper") List<String> timeKeeper,
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate);

    @Query(value = "select \n"
            + "	ar.CLASS_ID classId,\n"
            + "	ar.CLIENT_ID clientId,\n"
            + "	ar.CLIENT_NAME clientName,\n"
            + "	ar.MATTER_NO matterNumber,\n"
            + "	ar.CASE_CAT_ID caseCategory,\n"
            + "	ar.CASE_SUB_CAT_ID caseSubCategory,\n"
            + "	ar.MATTER_TEXT matterText,\n"
            + "	ar.BILLING_FORMAT billingFormat,\n"
            + "	ar.PHONE phone,\n"
            + "	ar.FEES_DUE feesDue,\n"
            + "	ar.HARD_COST_DUE hardCostsDue,\n"
            + "	ar.SOFT_COST_DUE softCostsDue,\n"
            + "	ar.TOTAL_DUE totalDue,\n"
            + "	ar.POSTING_DATE postingDate,\n"
            + "	ar.LAST_BILL_DATE lastBillDate,\n"
            + "	ar.LAST_PAYMENT_DATE lastPaymentDate,\n"
            + "	ar.PARTNER partner,\n"
            + "	ar.ORIGINATING_TK originatingTimeKeeper,\n"
            + "	ar.RESPONSIBLE_TK responsibleTimeKeeper,\n"
            + "	ar.ASSIGNED_TK assignedTimeKeeper,\n"
            + "	ar.LEGAL_ASSIST legalAssistant,\n"
            + "	ar.PARALEGEL paralegal,\n"
            + "	ar.LAW_CLERK lawClerk\n"
            + "	from tblarreport ar\n"
            + "	left join tblmattergenaccid mg on mg.matter_no = ar.matter_no \n"
            + "	where \n"
            + "(COALESCE(:classId,null) IS NULL OR (ar.CLASS_ID IN (:classId))) and \n"
            + "(COALESCE(:clientId,null) IS NULL OR (ar.CLIENT_ID IN (:clientId))) and \n"
            + "(COALESCE(:matterNumber,null) IS NULL OR (ar.MATTER_NO IN (:matterNumber))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (ar.CASE_CAT_ID IN (:caseCategory))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (ar.CASE_SUB_CAT_ID IN (:caseSubCategory))) and \n"
            + "(COALESCE(:fromDate,null) IS NULL OR (ar.POSTING_DATE BETWEEN :fromDate AND :toDate)) and \n"
            + "(COALESCE(:timeKeeper,null) IS NULL OR ar.PARTNER IN (:timeKeeper) OR ar.ORIGINATING_TK IN (:timeKeeper) \n"
            + "OR ar.RESPONSIBLE_TK IN (:timeKeeper) OR ar.ASSIGNED_TK IN (:timeKeeper) OR ar.LEGAL_ASSIST IN (:timeKeeper) \n"
            + "OR ar.PARALEGEL IN (:timeKeeper) OR ar.LAW_CLERK IN (:timeKeeper)) and mg.status_id <> 30 ", nativeQuery = true)
    public List<ARReportImpl> getARReportWithoutClosedMatter(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "clientId") List<String> clientId,
            @Param(value = "matterNumber") List<String> matterNumber,
            @Param(value = "caseCategory") List<Long> caseCategory,
            @Param(value = "caseSubCategory") List<Long> caseSubCategory,
            @Param(value = "timeKeeper") List<String> timeKeeper,
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate);
}