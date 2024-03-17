package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

import com.mnrclara.api.accounting.model.impl.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.prebill.MatterGenAcc;

@Repository
@Transactional
public interface MatterGenAccRepository extends JpaRepository<MatterGenAcc, Long>, JpaSpecificationExecutor<MatterGenAcc>,
DynamicNativeQuery {

    public List<MatterGenAcc> findByMatterNumberIn(List<String> matterNumber);
    public List<MatterGenAcc> findByClassIdIn(List<Long> classId);
    public MatterGenAcc findByMatterNumberAndClassId(String matterNumber, Long classId);

    @Query(value = "select  \n"
            + "mg.class_id as classId,  \n"
            + "mg.MATTER_NO as matterNumber,  \n"
            + "ts.status_text as statusId,  \n"
            + "mg.MATTER_TEXT as matterText, \n"
            + "mg.CLIENT_ID as clientId, \n"
            + "cg.FIRST_LAST_NM as firstLastName, \n"
            + "cg.client_cat_id clientCategoryId, \n"
            + "(case when cg.corp_client_id is not null then \n"
            + "(select first_last_nm from tblclientgeneralid where is_deleted = 0 and client_id=cg.corp_client_id) else null end) corporateName,\n"
            + "(case when cg.ref_field_2 is not null then \n"
            + "(select first_last_nm from tblclientgeneralid where is_deleted = 0 and client_id=cg.ref_field_2) else null end) petitionerName, \n"
            + "mg.CASE_OPEN_DATE caseOpenedDate, \n"
            + "mg.CASE_CLOSED_DATE as caseClosedDate, \n"
            + "mg.BILL_MODE_ID as billModeId,  \n"
            + "bm.bill_mode_text as billModeText, \n"
            + "bf.bill_freq_text billFrequencyText, \n"
            + "mg.CASE_CATEGORY_ID as caseCategoryId, \n"
            + "c.case_category as caseCategory, \n"
            + "mg.CASE_SUB_CATEGORY_ID as caseSubCategoryId, \n"
            + "cs.case_sub_category as caseSubCategory, \n"
            + "ma.responsible_tk as responsibleTk,  \n"
            + "ma.originating_tk as originatingTk,  \n"
            + "ma.LEGAL_ASSIST as legalAssist,  \n"
            + "ma.REF_FIELD_2 as paraLegal,  \n"
            + "ma.ASSIGNED_TK as assignedTk,  \n"
            + "ma.PARTNER as partner  \n"
            + "from tblmattergenaccid as mg \n"
            + "left join tblclientgeneralid cg on mg.CLIENT_ID  = cg.CLIENT_ID \n"
            + "left join tblstatusid ts on ts.status_id = mg.status_id \n"
            + "left join tblbillingmodeid bm on mg.bill_mode_id = bm.bill_mode_id \n"
            + "left join tblbillingfrequencyid bf on bf.bill_freq_id = mg.bill_freq_id \n"
            + "left join tblcasecategoryid c on mg.case_category_id = c.case_category_id \n"
            + "left join tblcasesubcategoryid cs on mg.case_sub_category_id = cs.case_sub_category_id \n"
            + "left join tblmatterassignmentid ma on mg.matter_no = ma.matter_no \n"
            + "where mg.CLASS_ID in :classId and mg.STATUS_ID in :statusId and \n"
            + "(COALESCE(:originatingTimeKeeper) IS NULL OR (ma.originating_tk IN (:originatingTimeKeeper))) and \n"
            + "(COALESCE(:responsibleTimeKeeper) IS NULL OR (ma.responsible_tk IN (:responsibleTimeKeeper))) and \n"
            + "(COALESCE(:assignedTimeKeeper) IS NULL OR (ma.assigned_tk IN (:assignedTimeKeeper))) and \n"
            + "(COALESCE(:legalAssistant) IS NULL OR (ma.legal_assist IN (:legalAssistant))) and \n"
            + "(COALESCE(:paralegal) IS NULL OR (ma.ref_field_2 IN (:paralegal))) and \n"
            + "(COALESCE(:partner) IS NULL OR (ma.partner IN (:partner))) and  \n"
            + "(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and  \n"
            + "(COALESCE(:caseSubCategoryId) IS NULL OR (mg.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId))) and \n"
            + "(COALESCE(:billFrequencyId) IS NULL OR (mg.BILL_FREQ_ID IN (:billFrequencyId))) and \n"
            + "(COALESCE(:petitionerName) IS NULL OR (cg.ref_field_2 IN (:petitionerName))) and \n"
            + "(COALESCE(:corporateName) IS NULL OR (cg.corp_client_id IN (:corporateName))) and \n"
            + " mg.is_deleted=0", nativeQuery = true)
    public List<MatterListingReportImpl> getAllMatterListing(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "originatingTimeKeeper") List<String> originatingTimeKeeper,
            @Param(value = "responsibleTimeKeeper") List<String> responsibleTimeKeeper,
            @Param(value = "assignedTimeKeeper") List<String> assignedTimeKeeper,
            @Param(value = "legalAssistant") List<String> legalAssistant,
            @Param(value = "paralegal") List<String> paralegal,
            @Param(value = "partner") List<String> partner,
            @Param(value = "caseCategoryId") List<Long> caseCategoryId,
            @Param(value = "caseSubCategoryId") List<Long> caseSubCategoryId,
            @Param(value = "billFrequencyId") List<String> billFrequencyId,
            @Param(value = "petitionerName") List<String> petitionerName,
            @Param(value = "corporateName") List<String> corporateName);

    @Query(value = "select mg.class_id as classId, mg.MATTER_NO as matterNumber , mg.MATTER_TEXT as matterText,\n" +
            "mg.CLIENT_ID as clientId ,cg.FIRST_LAST_NM as firstLastName,\n" +
            "mg.BILL_MODE_ID as billModeId, bm.bill_mode_text as billModeText,\n" +
            "mg.BILL_FREQ_ID,bf.bill_freq_text as billFrequencyText,\n" +
            "mr.TK_CODE as timeKeeperCode, mr.ASSIGNED_RATE as assignedRate\n" +
            "from tblmattergenaccid as mg\n" +
            "left join tblclientgeneralid cg on mg.CLIENT_ID  = cg.CLIENT_ID \n" +
            "left join tblbillingmodeid bm on mg.bill_mode_id = bm.bill_mode_id\n" +
            "left join tblbillingfrequencyid bf on mg.bill_freq_id= bf.bill_freq_id\n" +
            "left join tblmatterrateid mr on mg.MATTER_NO = mr.MATTER_NO\n" +
            "left join tbltimekeepercodeid tk on mr.TK_CODE = tk.TK_CODE\n" +
            "where mg.CLASS_ID in :classId and mg.STATUS_ID in :statusId and\n" +
            "(COALESCE(:clientId) IS NULL OR (mg.CLIENT_ID IN (:clientId))) and \n" +
            "(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n" +
            "(COALESCE(:caseSubCategoryId) IS NULL OR (mg.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId))) and\n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR (mr.TK_CODE IN (:timeKeeperCode))) and \n" +
            "(COALESCE(:timeKeeperStatus) IS NULL OR (tk.TK_STATUS IN (:timeKeeperStatus))) and\n" +
            "(COALESCE(:billFrequencyId) IS NULL OR (mg.BILL_FREQ_ID IN (:billFrequencyId))) ", nativeQuery = true)
    public List<MatterListingReportImpl> getAllMatterRatesListing(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "clientId") List<String> clientId,
            @Param(value = "caseCategoryId") List<Long> caseCategoryId,
            @Param(value = "caseSubCategoryId") List<Long> caseSubCategoryId,
            @Param(value = "timeKeeperCode") List<String> timeKeeperCode,
            @Param(value = "timeKeeperStatus") List<String> timeKeeperStatus,
            @Param(value = "billFrequencyId") List<String> billFrequencyId);

    @Query(value = "select mt.CLASS_ID as classId,\n" +
            "cc.case_category as caseCategory,cs.case_sub_category as caseSubCategory,\n" +
            "mt.TK_CODE as timeKeeperCode,\n" +
            "SUM(CASE WHEN mt.BILL_TYPE = 'billable' THEN mt.APP_BILL_TIME ELSE 0 END) as billableHours,\n" +
            "SUM(CASE WHEN mt.BILL_TYPE = 'non-billable' THEN mt.APP_BILL_TIME ELSE 0 END) as nonBillableHours,\n" +
            "SUM(CASE WHEN mt.BILL_TYPE in('NoCharge','No%20Charge','No Charge') THEN mt.APP_BILL_TIME ELSE 0 END) as noCharge,\n" +
            "SUM(mt.APP_BILL_TIME) as totalHours,SUM(mt.TIME_TICKET_HRS) as totalTimeTicketHours\n" +
            "from tblmattertimeticketid mt\n" +
            "left join tblmattergenaccid mg on mt.MATTER_NO = mg.MATTER_NO\n" +
            "left join tblcasecategoryid cc on mg.case_category_id = cc.case_category_id\n" +
            "left join tblcasesubcategoryid cs on mg.case_sub_category_id = cs.case_sub_category_id\n" +
            "where mt.CLASS_ID in :classId and \n" +
            "(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n" +
            "(COALESCE(:caseSubCategoryId) IS NULL OR (mg.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId))) and\n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR (mt.TK_CODE IN (:timeKeeperCode))) and \n" +
            "mt.TIME_TICKET_DATE BETWEEN :fromDate AND :toDate and mt.is_deleted = 0 and (mt.status_id=51 or mt.status_id=62)\n" +
            "group by mt.CLASS_ID, mg.CASE_CATEGORY_ID , mg.CASE_SUB_CATEGORY_ID,mt.TK_CODE ", nativeQuery = true)
    public List<BilledUnBillerHoursReportImpl> getBilledUnBilledHours(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate,
            @Param(value = "timeKeeperCode") List<String> timeKeeperCode,
            @Param(value = "caseCategoryId") List<Long> caseCategoryId,
            @Param(value = "caseSubCategoryId") List<Long> caseSubCategoryId);

    @Query(value = "select \n" +
            "pu.MATTER_NO as matterNumber ,mg.MATTER_TEXT as matterText,\n" +
            "mg.client_id as clientId,cg.FIRST_LAST_NM as clientName,\n" +
            "max(pu.payment_date) as paymentDate , sum(pu.payment_amount) as paymentAmount\n" +
            "from tblpaymentupdate pu\n" +
            "join tblmattergenaccid mg on pu.matter_no = mg.MATTER_NO\n" +
            "join tblclientgeneralid cg on mg.client_id = cg.CLIENT_ID\n" +
            "where \n" +
            "(COALESCE(:matterNumber) IS NULL OR (pu.MATTER_NO IN (:matterNumber))) and \n" +
            "(COALESCE(:clientId) IS NULL OR (mg.client_id IN (:clientId))) and\n" +
            "(COALESCE(:fromDate) IS NULL OR (pu.payment_date between :fromDate and :toDate)) \n" +
            " AND pu.is_deleted = 0 and mg.is_deleted = 0 and cg.is_deleted = 0 \n" +
            "group by pu.MATTER_NO ,mg.MATTER_TEXT,mg.client_id,cg.FIRST_LAST_NM ", nativeQuery = true)
    public List<ClientCashReceiptsReportImpl> getClientCashReceipts(
            @Param(value = "matterNumber") List<String> matterNumber,
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate,
            @Param(value = "clientId") List<String> clientId);

    @Query(value = "select \n" +
            "pu.client_id as clientId,cg.FIRST_LAST_NM as clientName, \n" +
            "sum(pu.payment_amount) as totalReceipts , \n" +
            "coalesce((select sum(me.EXP_AMOUNT) from tblmatterexpenseid me  \n" +
            "where me.exp_code='REF' and is_deleted = 0 and \n" +
            "me.client_id = pu.client_id and \n" +
            "(COALESCE(:fromDate) IS NULL OR (me.ref_field_2 between :fromDate and :toDate))),0) as totalRefund,  \n" +
            "(sum(pu.payment_amount)- \n" +
            "abs(coalesce((select sum(me1.EXP_AMOUNT) from tblmatterexpenseid me1  \n" +
            "where me1.exp_code='REF' and is_deleted = 0 and \n" +
            "me1.client_id = pu.client_id and \n" +
            "(COALESCE(:fromDate) IS NULL OR (me1.ref_field_2 between :fromDate and :toDate))),0))) as netReceipts \n" +
            "from tblpaymentupdate pu \n" +
            "join tblclientgeneralid cg on pu.client_id = cg.CLIENT_ID \n" +
            "where \n" +
            "(COALESCE(:clientId) IS NULL OR (pu.client_id IN (:clientId))) and \n" +
            "(COALESCE(:fromDate) IS NULL OR (pu.payment_date between :fromDate and :toDate)) \n" +
            "group by pu.client_id,cg.FIRST_LAST_NM", nativeQuery = true)
    public List<ClientIncomeSummaryReportImpl> getClientIncomeSummary(
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate,
            @Param(value = "clientId") List<String> clientId);

    @Query(value = "select \n" +
            "me.MATTER_NO as matterNumber ,mg.MATTER_TEXT as matterText,\n" +
            "me.client_id as clientId,cg.FIRST_LAST_NM as clientName,\n" +
            "mg.case_open_date as date , ma.responsible_tk as responsibleTimeKeeper, \n" +
            "SUM(CASE WHEN me.EXP_CODE in ('WFF','WOF') THEN me.EXP_AMOUNT ELSE 0 END) as fees ,\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WHC' THEN me.EXP_AMOUNT ELSE 0 END) as hardCosts ,\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WSC' THEN me.EXP_AMOUNT ELSE 0 END) as softCosts ,\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WOT' THEN me.EXP_AMOUNT ELSE 0 END) as taxes ,\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WOL' THEN me.EXP_AMOUNT ELSE 0 END) as lateCharges,\n" +
            "(SUM(CASE WHEN me.EXP_CODE in ('WFF','WOF') THEN me.EXP_AMOUNT ELSE 0 END) +\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WHC' THEN me.EXP_AMOUNT ELSE 0 END) +\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WSC' THEN me.EXP_AMOUNT ELSE 0 END) +\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WOT' THEN me.EXP_AMOUNT ELSE 0 END) +\n" +
            "SUM(CASE WHEN me.EXP_CODE = 'WOL' THEN me.EXP_AMOUNT ELSE 0 END)) as total\n" +
            "from tblmatterexpenseid me\n" +
            "left join tblmattergenaccid mg on me.matter_no = mg.MATTER_NO\n" +
            "left join tblclientgeneralid cg on me.client_id = cg.CLIENT_ID\n" +
            "left join tblmatterassignmentid ma on me.matter_no = ma.matter_no\n" +
            "where \n" +
            "(COALESCE(:matterNumber) IS NULL OR (me.MATTER_NO IN (:matterNumber))) and \n" +
            "(COALESCE(:clientId) IS NULL OR (me.client_id IN (:clientId))) and\n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR (ma.responsible_tk IN (:timeKeeperCode))) and\n" +
            "(me.ctd_on BETWEEN :fromDate AND :toDate) and me.EXP_CODE in ('WFF', 'WHC','WSC','WOF','WOL','WOT') \n" +
            "group by me.MATTER_NO,mg.MATTER_TEXT,me.client_id,cg.FIRST_LAST_NM,mg.case_open_date, ma.responsible_tk ", nativeQuery = true)
    public List<WriteOffReportImpl> getWriteOffReport(
            @Param(value = "matterNumber") List<String> matterNumber,
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate,
            @Param(value = "clientId") List<String> clientId,
            @Param(value = "timeKeeperCode") List<String> timeKeeperCode);

//	@Query(value = "select \n" +
//			"tt.MATTER_NO as matterNumber ,mg.MATTER_TEXT as matterText,\n" +
//			"ih.invoice_no as invoiceNumber , ih.posting_date as dateOfBill,\n" +
//			"tt.TK_CODE as attorney,\n" +
//			"sum(tt.APP_BILL_TIME) as hoursBilled , sum(tt.APP_BILL_AMOUNT) as amountBilled,\n" +
//			"((sum(ih.paid_amount) / sum(ih.invoice_amt)) * sum(tt.APP_BILL_TIME)) as approxHoursPaid,\n" +
//			"(((sum(ih.paid_amount) / sum(ih.invoice_amt)) * sum(tt.APP_BILL_TIME)) * sum(mr.ASSIGNED_RATE)) as amountReceived\n" +
//			"from tblmattertimeticketid tt\n" +
//			"left join tblmattergenaccid mg on tt.matter_no = mg.MATTER_NO and mg.STATUS_ID in (46,51)\n" +
//			"left join tblinvoiceheader ih on tt.matter_no = ih.matter_no\n" +
//			"left join tbltimekeepercodeid tk on tt.TK_CODE = tk.TK_CODE and tk.usr_typ_id in (2) \n" +
//			"left join tblmatterrateid mr on tt.TK_CODE = mr.TK_CODE and tt.MATTER_NO = mr.MATTER_NO \n" +
//			"where \n" +
//			"(COALESCE(:matterNumber) IS NULL OR (tt.MATTER_NO IN (:matterNumber))) and \n" +
//			"(COALESCE(:clientId) IS NULL OR (tt.client_id IN (:clientId))) and\n" +
//			"(COALESCE(:timeKeeperCode) IS NULL OR (tt.TK_CODE IN (:timeKeeperCode))) and\n" +
//			"ih.posting_date BETWEEN :fromPostingDate AND :toPostingDate and ih.IS_DELETED=0 and \n" +
//			"(COALESCE(:fromTimeKeeperDate) IS NULL OR (tt.TIME_TICKET_DATE between :fromTimeKeeperDate and :toTimeKeeperDate)) \n" +
//			"group by tt.MATTER_NO,mg.MATTER_TEXT,ih.invoice_no,ih.posting_date,tt.TK_CODE ", nativeQuery = true)
//	public List<BilledHoursPaidImpl> getBilledHoursPaidReport (
//			@Param(value = "matterNumber") List<String> matterNumber,
//			@Param(value = "fromPostingDate") Date fromPostingDate,
//			@Param(value = "toPostingDate") Date toPostingDate,
//			@Param(value = "fromTimeKeeperDate") Date fromTimeKeeperDate,
//			@Param(value = "toTimeKeeperDate") Date toTimeKeeperDate,
//			@Param(value = "clientId") List<String> clientId,
//			@Param(value = "timeKeeperCode") List<String> timeKeeperCode);

    @Query(value = "select \n" +
            "A.case_category caseCategory,A.case_sub_category caseSubCategory,A.tk_code timeKeeper,\n" +
            "A.billedSumA as billedSumA, B.billedSumB as billedSumB,\n" +
            "(billedSumA / billedSumB)*100 as billedPercent ,\n" +
            "A.paidAmount as paidAmount, A.billAmount as billAmount, B.totalTime as totalTime, B.assignedRate as assignedRate,\n" +
            "ifnull((((A.paidAmount / A.billAmount) * B.totalTime) / B.assignedRate) * 100,0) as paidPercent,\n" +
            "A.billedYearToDateA as billedYearToDateA,A.billedYearToDateB as billedYearToDateB, \n" +
            "ifnull(((A.billedYearToDateB/A.billedYearToDateA) * 100),0) as billedPercentYearToDate ,\n" +
            "A.paidYearToDate as paidYearToDate,A.billYearToDate as billYearToDate, B.totalTimeYearToDate as totalTimeYearToDate, B.assignedRateYearToDate as assignedRateYearToDate,\n" +
            " ifnull((((A.paidYearToDate / A.billYearToDate) * B.totalTimeYearToDate) / B.assignedRateYearToDate) * 100,0) as paidPercentYearToDate\n" +
            "from (\n" +
            "select cc.case_category,sc.case_sub_category,mt.tk_code,ifnull(sum(mt.APP_BILL_AMOUNT),0) billedSumA,\n" +
            "ih.matter_no as matterNumber , ifnull(sum(ih.invoice_amt),0) billAmount, ifnull(sum(pu.payment_amount),0) paidAmount,\n" +
            "( select ifnull(sum(mtyA.APP_BILL_AMOUNT),0) from tblmattertimeticketid mtyA\n" +
            "join tblmattergenaccid mgy on mtyA.MATTER_NO = mgy.MATTER_NO\n" +
            "join tblcasecategoryid ccy on mgy.case_category_id = ccy.case_category_id\n" +
            "join tblcasesubcategoryid scy on mgy.case_sub_category_id = scy.case_sub_category_id\n" +
            "where ccy.case_category = cc.case_category and scy.case_sub_category = sc.case_sub_category\n" +
            "and mtyA.tk_code = mt.tk_code and mtyA.TIME_TICKET_DATE between  :fromCurrentYear and :toDate  ) as billedYearToDateA ,\n" +
            "( select \n" +
            "ifnull(sum(ihy.invoice_amt),0) from tblmattertimeticketid mtyA\n" +
            "join tblmattergenaccid mgy on mtyA.MATTER_NO = mgy.MATTER_NO\n" +
            "join tblcasecategoryid ccy on mgy.case_category_id = ccy.case_category_id\n" +
            "join tblcasesubcategoryid scy on mgy.case_sub_category_id = scy.case_sub_category_id\n" +
            "left join tblinvoiceheader ihy on mgy.matter_no = ihy.matter_no\n" +
            "where ccy.case_category = cc.case_category and scy.case_sub_category = sc.case_sub_category\n" +
            "and mtyA.tk_code = mt.tk_code and mtyA.TIME_TICKET_DATE between  :fromCurrentYear and :toDate ) as billYearToDate ,\n" +
            "(select \n" +
            "ifnull(sum(puy.payment_amount),0) from tblmattertimeticketid mtyB\n" +
            "join tblmattergenaccid mgy on mtyB.MATTER_NO = mgy.MATTER_NO\n" +
            "join tblcasecategoryid ccy on mgy.case_category_id = ccy.case_category_id\n" +
            "join tblcasesubcategoryid scy on mgy.case_sub_category_id = scy.case_sub_category_id\n" +
            "left join tblinvoiceheader ihy on mgy.matter_no = ihy.matter_no\n" +
            "left join tblpaymentupdate puy on ihy.matter_no = puy.matter_no\n" +
            "where ccy.case_category = cc.case_category and scy.case_sub_category = sc.case_sub_category\n" +
            "and mtyB.tk_code = mt.tk_code and mtyB.TIME_TICKET_DATE between  :fromCurrentYear and :toDate ) as paidYearToDate ,\n" +
            "(select ifnull(sum(mtyB.APP_BILL_AMOUNT),0) from tblmattertimeticketid mtyB\n" +
            "where mtyB.tk_code = mt.tk_code and mtyB.TIME_TICKET_DATE between  :fromCurrentYear and :toDate ) as billedYearToDateB\n" +
            "from tblmattertimeticketid mt\n" +
            "join tblmattergenaccid mg on mt.MATTER_NO = mg.MATTER_NO\n" +
            "join tblcasecategoryid cc on mg.case_category_id = cc.case_category_id\n" +
            "join tblcasesubcategoryid sc on mg.case_sub_category_id = sc.case_sub_category_id\n" +
            "left join tblinvoiceheader ih on mg.matter_no = ih.matter_no\n" +
            "left join tblpaymentupdate pu on ih.matter_no = pu.matter_no\n" +
            "where mt.TIME_TICKET_DATE between :fromDate and :toDate and \n" +
            "(COALESCE(:caseCategoryId) IS NULL OR (mg.case_category_id IN (:caseCategoryId))) and \n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR (mt.tk_code IN (:timeKeeperCode))) and \n" +
            " mt.STATUS_ID in (46,51)\n" +
            "group by cc.case_category,sc.case_sub_category,mt.tk_code,ih.matter_no ) A \n" +
            "inner join \n" +
            "(select mt.TK_CODE,ifnull(sum(mt.APP_BILL_AMOUNT),0) billedSumB ,ifnull(sum(mt.APP_BILL_TIME),0) totalTime,\n" +
            "ifnull(sum(mr.ASSIGNED_RATE),0) as assignedRate ,\n" +
            "(select ifnull(sum(mtPB.APP_BILL_TIME),0) from tblmattertimeticketid mtPB where mtPB.tk_code = mt.tk_code and \n" +
            "mtPB.TIME_TICKET_DATE between  :fromCurrentYear and :toDate) as totalTimeYearToDate,\n" +
            "(select ifnull(sum(mrPB.ASSIGNED_RATE),0) from tblmatterrateid mrPB \n" +
            "join tblmattertimeticketid mtPB on mtPB.matter_no = mrPB.matter_no\n" +
            "where mrPB.tk_code = mt.tk_code and mtPB.TIME_TICKET_DATE between  :fromCurrentYear and :toDate ) as assignedRateYearToDate \n" +
            "from tblmattertimeticketid mt\n" +
            "join tblmattergenaccid mg on mt.MATTER_NO = mg.MATTER_NO\n" +
            "join tblmatterrateid mr on mt.MATTER_NO = mr.MATTER_NO and mt.TK_CODE = mr.TK_CODE\n" +
            "where mt.TIME_TICKET_DATE between :fromDate and :toDate and \n" +
            "(COALESCE(:caseCategoryId) IS NULL OR (mg.case_category_id IN (:caseCategoryId))) and \n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR (mt.tk_code IN (:timeKeeperCode))) and \n" +
            " mt.STATUS_ID in (46,51)\n" +
            "group by mt.TK_CODE) B on A.tk_code = B.TK_CODE\n" +
            "group by A.case_category,A.case_sub_category,A.tk_code,A.matterNumber ", nativeQuery = true)
    public List<BilledPaidFeesImpl> getBilledPaidFeesReport(
            @Param(value = "fromDate") Date fromDate,
            @Param(value = "toDate") Date toDate,
            @Param(value = "caseCategoryId") List<Long> caseCategoryId,
            @Param(value = "timeKeeperCode") List<String> timeKeeperCode,
            @Param(value = "fromCurrentYear") Date fromCurrentYear);

    @Query(value = "select \n" +
            "cg.FIRST_LAST_NM as clientName, mg.matter_text as matterText, mg.matter_no matterNumber, ed.reminder_text reminderDescription, \n" +
            "CASE \n" +
            "when cg.CLIENT_CAT_ID = 4 \n" +
            "THEN (select FIRST_LAST_NM from tblclientgeneralid where client_id = cg.REF_FIELD_2)\n" +
            "when cg.CLIENT_CAT_ID = 3 \n" +
            "THEN (select FIRST_LAST_NM from tblclientgeneralid where client_id = cg.CORP_CLIENT_ID)\n" +
            "ELSE null END as employerPetitionerName ,\n" +
            "ed.doc_type as docType , ed.eligibility_date as eligibilityDate , ed.approval_date as approvalDate , \n" +
            "ed.expiration_date as expirationDate , ma.originating_tk as originatingTk , ma.responsible_tk as responsibleTk ,\n" +
            "ma.assigned_tk as assignedTk , ma.ref_field_2 as paralegal , ma.legal_assist as legalAssistant, rn.receipt_no receiptNumber\n" +
            "from tblexpirationdate ed \n" +
            "left join tblmatterassignmentid ma on ed.matter_no = ma.matter_no\n" +
            "left join tblclientgeneralid cg on ed.client_id = cg.CLIENT_ID\n" +
            "left join tblmattergenaccid mg on ed.matter_no = mg.MATTER_NO  \n" +
            "left join tblreceiptappnotice rn on ed.matter_no = rn.matter_no and ed.doc_type = rn.doc_type  \n" +
            "where \n" +
            "(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n" +
            "(COALESCE(:caseSubCategoryId) IS NULL OR (mg.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId))) and\n" +
            "(COALESCE(:receiptNumber) IS NULL OR (rn.receipt_no IN (:receiptNumber))) and\n" +
            "(COALESCE(:timeKeeperCode) IS NULL OR \n" +
            "((ma.originating_tk IN (:timeKeeperCode)) or (ma.responsible_tk IN (:timeKeeperCode)) or \n" +
            "(ma.assigned_tk IN (:timeKeeperCode)) or (ma.legal_assist IN (:timeKeeperCode)))) and\n" +
            "(COALESCE(:documentType) IS NULL OR (ed.doc_type in (:documentType))) and\n" +
            "(COALESCE(:fromEligibilityDate) IS NULL OR (ed.eligibility_date between :fromEligibilityDate and :toEligibilityDate)) and \n" +
            "(COALESCE(:fromExpirationDate) IS NULL OR (ed.expiration_date between :fromExpirationDate AND :toExpirationDate)) and ed.is_deleted = 0 ", nativeQuery = true)
    public List<ExpirationDateReportImpl> getExpirationDateReport(
            @Param(value = "caseCategoryId") List<Long> caseCategoryId,
            @Param(value = "caseSubCategoryId") List<Long> caseSubCategoryId,
            @Param(value = "receiptNumber") List<String> receiptNumber,
            @Param(value = "timeKeeperCode") List<String> timeKeeperCode,
            @Param(value = "documentType") List<String> documentType,
            @Param(value = "fromExpirationDate") Date fromExpirationDate,
            @Param(value = "toExpirationDate") Date toExpirationDate,
            @Param(value = "fromEligibilityDate") Date fromEligibilityDate,
            @Param(value = "toEligibilityDate") Date toEligibilityDate
    );

    public MatterGenAcc findByMatterNumber(String matterNumber);

    public MatterGenAcc findByMatterNumberAndClassIdIn(String matterNumber, List<Long> asList);

    @Query(value = "SELECT BILL_FORMAT_TEXT FROM tblbillingformatid WHERE BILL_FORMAT_ID = :billFormatID", nativeQuery = true)
    public String getBillFormatText(@Param(value = "billFormatID") Long billFormatID);
}