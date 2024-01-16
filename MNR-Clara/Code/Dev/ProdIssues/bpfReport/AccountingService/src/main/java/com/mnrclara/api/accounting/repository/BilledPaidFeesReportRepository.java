package com.mnrclara.api.accounting.repository;

import com.mnrclara.api.accounting.model.reports.ARReport;
import com.mnrclara.api.accounting.model.reports.BilledPaidFeesReportImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface BilledPaidFeesReportRepository extends JpaRepository<ARReport, Long>, JpaSpecificationExecutor<ARReport> {

    //truncate report table
    @Modifying
    @Transactional
    @Query(value = "truncate table tblbpfreport ", nativeQuery = true)
    public void truncateReportTable();

    //populate timekeeper code
    @Modifying
    @Transactional
    @Query(value = "insert into tblbpfreport (tk_code,tk_name,case_category_id,case_sub_category_id)\n"
            + "	(\n"
            + "	SELECT distinct tkc.tk_code, tkc.tk_name, mg.case_category_id,mg.case_sub_category_id \n"
            + "	FROM tbltimekeepercodeid tkc\n"
            + "	left join tblmattertimeticketid tmt on tmt.tk_code=tkc.tk_code \n"
            + "	left join tblmattergenaccid mg on mg.matter_no=tmt.matter_no \n"
            + "	where \n"
            + "	(COALESCE(:timeKeeperCode) IS NULL OR (tkc.tk_code IN (:timeKeeperCode))) and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n"
            + "	tkc.is_deleted=0\n"
            + "	)", nativeQuery = true)
    public void getTimeKeeperCodeTable(@Param(value = "caseCategoryId") List<Long> caseCategoryId,
                                       @Param(value = "timeKeeperCode") List<String> timeKeeperCode);

    //update monthly billed amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	SELECT tt.tk_code, mg.case_sub_category_id, mg.case_category_id, sum(COALESCE(tt.APP_bill_amount,0)) mBilledAmount \n"
            + "	FROM tblmattertimeticketid tt \n"
            + "	join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where tt.status_id = 51 and tt.is_deleted=0 and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate \n"
            + "	GROUP BY mg.case_sub_category_id, mg.case_category_id, tt.tk_code \n"
            + "	) x ON tta.tk_code = x.tk_code and tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.MONTH_BILLED_AMOUNT = x.mBilledAmount ", nativeQuery = true)
    public void updateMonthlyBilledAmount(@Param(value = "fromDate") Date fromDate,
                                          @Param(value = "toDate") Date toDate);

    //update monthly timeticket amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	SELECT tt.tk_code, mg.case_sub_category_id, mg.case_category_id, sum(COALESCE(tt.TIME_TICKET_AMOUNT,0)) mApprovedAmount\n"
            + "	FROM tblmattertimeticketid tt \n"
            + "	join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where tt.is_deleted=0 and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate \n"
            + "	GROUP BY mg.case_sub_category_id, mg.case_category_id, tt.tk_code \n"
            + "	) x ON tta.tk_code = x.tk_code and tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.MONTH_TIMETICKET_AMOUNT = x.mApprovedAmount ", nativeQuery = true)
    public void updateMonthlyApprovedAmount(@Param(value = "fromDate") Date fromDate,
                                            @Param(value = "toDate") Date toDate);

    //update yearly billed amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta \n"
            + "	JOIN (\n"
            + "	SELECT tt.tk_code, mg.case_sub_category_id, mg.case_category_id, sum(COALESCE(tt.APP_BILL_AMOUNT,0)) yBilledAmount\n"
            + "	FROM tblmattertimeticketid tt\n"
            + "	join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where tt.status_id = 51 and tt.is_deleted=0 and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate \n"
            + "	GROUP BY mg.case_sub_category_id, mg.case_category_id, tt.tk_code \n"
            + "	) x ON tta.tk_code = x.tk_code and tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.YEAR_BILLED_AMOUNT = x.yBilledAmount ", nativeQuery = true)
    public void updateYearlyBilledAmount(@Param(value = "fromDate") Date fromDate,
                                         @Param(value = "toDate") Date toDate);

    //update yearly timeticket amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta \n"
            + "	JOIN (\n"
            + "	SELECT tt.tk_code, mg.case_sub_category_id, mg.case_category_id, sum(COALESCE(tt.TIME_TICKET_AMOUNT,0)) yApprovedAmount\n"
            + "	FROM tblmattertimeticketid tt\n"
            + "	join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where tt.is_deleted=0 and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate \n"
            + "	GROUP BY mg.case_sub_category_id, mg.case_category_id, tt.tk_code \n"
            + "	) x ON tta.tk_code = x.tk_code and tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.YEAR_TIMETICKET_AMOUNT = x.yApprovedAmount ", nativeQuery = true)
    public void updateYearlyApprovedAmount(@Param(value = "fromDate") Date fromDate,
                                           @Param(value = "toDate") Date toDate);

    //truncate Intermediate report table for Monthly Invoice Amount(MIA) Calculation
    @Modifying
    @Transactional
    @Query(value = "truncate table tblbpfreporttemp ", nativeQuery = true)
    public void truncateIntermediateReportTableMIA();

    //update monthly Invoice amount in the Intermediate report table
    @Modifying
    @Transactional
    @Query(value = "insert into tblbpfreporttemp (amount,case_category_id,case_sub_category_id)\n"
            + "	(\n"
            + "	select sum(ih.invoice_amt) amount,tmg.case_category_id,tmg.case_sub_category_id from tblinvoiceheader ih \n"
            + "	left join tblmattergenaccid tmg on tmg.matter_no=ih.matter_no \n"
            + "	where ih.is_deleted =0 and ih.matter_no in \n"
            + "	(select distinct tt.matter_no from tblmattertimeticketid tt \n"
            + "	left join tblmattergenaccid mg on mg.matter_no=tt.matter_no where tt.is_deleted=0 and tt.status_id = 51 and tt.time_ticket_date between \n"
            + "	:fromDate and :toDate and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId)))) and \n"
            + "	ih.posting_date between :fromDate and :toDate "
            + "	GROUP BY tmg.case_sub_category_id,tmg.case_category_id \n"
            + "	) ", nativeQuery = true)
    public void getMonthlyInvoiceAmount(@Param(value = "fromDate") Date fromDate,
                                        @Param(value = "toDate") Date toDate,
                                        @Param(value = "caseCategoryId") List<Long> caseCategoryId);

    //update monthly Invoice amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	select * from tblbpfreporttemp \n"
            + "	) x ON tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.MONTH_INVOICE_AMOUNT = x.amount\n", nativeQuery = true)
    public void updateMonthlyInvoiceAmount();

    //truncate Intermediate report table for Yearly Invoice Amount(YIA) Calculation
    @Modifying
    @Transactional
    @Query(value = "truncate table tblbpfreporttemp ", nativeQuery = true)
    public void truncateIntermediateReportTableYIA();

    //update yearly invoice amount in the report Intermediate table
    @Modifying
    @Transactional
    @Query(value = "insert into tblbpfreporttemp (amount,case_category_id,case_sub_category_id)\n"
            + "	(\n"
            + "	select sum(ih.invoice_amt) amount,tmg.case_category_id,tmg.case_sub_category_id from tblinvoiceheader ih \n"
            + "	left join tblmattergenaccid tmg on tmg.matter_no=ih.matter_no \n"
            + "	where ih.is_deleted =0 and ih.matter_no in \n"
            + "	(select distinct tt.matter_no from tblmattertimeticketid tt \n"
            + "	left join tblmattergenaccid mg on mg.matter_no=tt.matter_no where tt.is_deleted=0 and tt.status_id = 51 and tt.time_ticket_date between \n"
            + "	:fromDate and :toDate and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId)))) and \n"
            + "	ih.posting_date between :fromDate and :toDate "
            + "	GROUP BY tmg.case_sub_category_id,tmg.case_category_id \n"
            + "	) ", nativeQuery = true)
    public void getYearlyInvoiceAmount(@Param(value = "fromDate") Date fromDate,
                                       @Param(value = "toDate") Date toDate,
                                       @Param(value = "caseCategoryId") List<Long> caseCategoryId);

    //update yearly Invoice amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	select * from tblbpfreporttemp \n"
            + "	) x ON tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.YEAR_INVOICE_AMOUNT = x.amount\n", nativeQuery = true)
    public void updateYearlyInvoiceAmount();

    //truncate Intermediate report table for Monthly Payment Amount(MPA) Calculation
    @Modifying
    @Transactional
    @Query(value = "truncate table tblbpfreporttemp ", nativeQuery = true)
    public void truncateIntermediateReportTableMPA();

    //update monthly payment amount in the report Intermediate table
    @Modifying
    @Transactional
    @Query(value = "insert into tblbpfreporttemp (amount,case_category_id,case_sub_category_id)\n"
            + "	(\n"
            + "	select sum(pu.payment_amount) amount,tmg.case_category_id,tmg.case_sub_category_id from tblpaymentupdate pu \n"
            + "	left join tblmattergenaccid tmg on tmg.matter_no=pu.matter_no \n"
            + "	where pu.is_deleted =0 and pu.matter_no in \n"
            + "	(select distinct tt.matter_no from tblmattertimeticketid tt \n"
            + "	left join tblmattergenaccid mg on mg.matter_no=tt.matter_no where tt.is_deleted=0 and tt.status_id = 51 and tt.time_ticket_date between \n"
            + "	:fromDate and :toDate and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId)))) and \n"
            + "	pu.payment_date between :fromDate and :toDate "
            + "	GROUP BY tmg.case_sub_category_id,tmg.case_category_id \n"
            + "	) ", nativeQuery = true)
    public void getMonthlyPaymentAmount(@Param(value = "fromDate") Date fromDate,
                                        @Param(value = "toDate") Date toDate,
                                        @Param(value = "caseCategoryId") List<Long> caseCategoryId);

    //update monthly payment amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	select * from tblbpfreporttemp \n"
            + "	) x ON tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.MONTH_PAYMENT_AMOUNT = x.amount\n", nativeQuery = true)
    public void updateMonthlyPaymentAmount();

    //truncate Intermediate report table for Yearly Payment Amount(YPA) Calculation
    @Modifying
    @Transactional
    @Query(value = "truncate table tblbpfreporttemp ", nativeQuery = true)
    public void truncateIntermediateReportTableYPA();

    //update monthly payment amount in the report Intermediate table
    @Modifying
    @Transactional
    @Query(value = "insert into tblbpfreporttemp (amount,case_category_id,case_sub_category_id)\n"
            + "	(\n"
            + "	select sum(pu.payment_amount) amount,tmg.case_category_id,tmg.case_sub_category_id from tblpaymentupdate pu \n"
            + "	left join tblmattergenaccid tmg on tmg.matter_no=pu.matter_no \n"
            + "	where pu.is_deleted =0 and pu.matter_no in \n"
            + "	(select tt.matter_no from tblmattertimeticketid tt \n"
            + "	left join tblmattergenaccid mg on mg.matter_no=tt.matter_no where tt.is_deleted=0 and tt.status_id = 51 and tt.time_ticket_date between \n"
            + "	:fromDate and :toDate and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId)))) and \n"
            + "	pu.payment_date between :fromDate and :toDate "
            + "	GROUP BY tmg.case_sub_category_id,tmg.case_category_id \n"
            + "	) ", nativeQuery = true)
    public void getYearlyPaymentAmount(@Param(value = "fromDate") Date fromDate,
                                       @Param(value = "toDate") Date toDate,
                                       @Param(value = "caseCategoryId") List<Long> caseCategoryId);

    //update yearly payment amount in the report table
    @Modifying
    @Transactional
    @Query(value = "UPDATE tblbpfreport tta\n"
            + "	JOIN (\n"
            + "	select * from tblbpfreporttemp \n"
            + "	) x ON tta.case_sub_category_id=x.case_sub_category_id and tta.case_category_id=x.case_category_id \n"
            + "	SET tta.YEAR_PAYMENT_AMOUNT = x.amount\n", nativeQuery = true)
    public void updateYearlyPaymentAmount();


    @Query(value = "select \n"
            + "	sum(invoice_amt) \n"
            + "	from tblinvoiceheader \n"
            + "	where \n"
            + "	matter_no in \n"
            + "	(select tt.matter_no from tblmattertimeticketid tt left join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where \n"
            + "	tt.is_deleted = 0 and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate) and \n"
            + "	posting_date between :fromDate and :toDate ", nativeQuery = true)
    public Double getInvoiceAmount(@Param(value = "fromDate") Date fromDate,
                                   @Param(value = "toDate") Date toDate,
                                   @Param(value = "caseCategoryId") List<Long> caseCategoryId);


    @Query(value = "select \n"
            + "	sum(payment_amount) \n"
            + "	from tblpaymentupdate \n"
            + "	where \n"
            + "	matter_no in \n"
            + "	(select tt.matter_no from tblmattertimeticketid tt left join tblmattergenaccid mg on mg.matter_no=tt.matter_no \n"
            + "	where \n"
            + "	tt.is_deleted = 0 and \n"
            + "	(COALESCE(:caseCategoryId) IS NULL OR (mg.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n"
            + "	tt.time_ticket_date between \n"
            + "	:fromDate and :toDate) and \n"
            + "	payment_date between :fromDate and :toDate ", nativeQuery = true)
    public Double getPaymentAmount(@Param(value = "fromDate") Date fromDate,
                                   @Param(value = "toDate") Date toDate,
                                   @Param(value = "caseCategoryId") List<Long> caseCategoryId);


    @Query(value = "select \n"
            + "	bp.TK_CODE timekeeperCode, \n"
            + "	bp.TK_NAME timekeeperName, \n"
            + "	concat(bp.CASE_CATEGORY_ID,'-',cc.CASE_CATEGORY) caseCategory, \n"
            + "	concat(bp.CASE_SUB_CATEGORY_ID,'-',cs.CASE_SUB_CATEGORY) caseSubCategory, \n"
            + "	bp.MONTH_BILLED_AMOUNT monthlyBilledAmount, \n"
            + "	bp.MONTH_TIMETICKET_AMOUNT monthlyTimeTicketAmount, \n"
            + "	bp.YEAR_BILLED_AMOUNT yearlyBilledAmount, \n"
            + "	bp.YEAR_TIMETICKET_AMOUNT yearlyTimeTicketAmount, \n"
            + "	bp.MONTH_INVOICE_AMOUNT monthlyInvoiceAmount, \n"
            + "	bp.YEAR_INVOICE_AMOUNT yearlyInvoiceAmount, \n"
            + "	bp.MONTH_PAYMENT_AMOUNT monthlyPaymentAmount, \n"
            + "	bp.YEAR_PAYMENT_AMOUNT yearlyPaymentAmount, \n"
            + "	Round(((COALESCE(bp.MONTH_BILLED_AMOUNT,0)/COALESCE(bp.MONTH_TIMETICKET_AMOUNT,0))*100),2) monthlyBilledPercentage,\n"
            + "	Round((COALESCE(bp.MONTH_BILLED_AMOUNT,0) * (COALESCE(bp.MONTH_PAYMENT_AMOUNT,0)/COALESCE(bp.MONTH_INVOICE_AMOUNT,0))),2) monthlyPaidAmount,\n"
            + "	Round((((COALESCE(bp.MONTH_BILLED_AMOUNT,0) * (COALESCE(bp.MONTH_PAYMENT_AMOUNT,0)/COALESCE(bp.MONTH_INVOICE_AMOUNT,0)))/COALESCE(bp.MONTH_BILLED_AMOUNT,0))*100),2) monthlyPaidPercentage,\n"
            + "	Round(((COALESCE(bp.YEAR_BILLED_AMOUNT,0)/COALESCE(bp.YEAR_TIMETICKET_AMOUNT,0))*100),2) yearlyBilledPercentage,\n"
            + "	Round((COALESCE(bp.YEAR_BILLED_AMOUNT) * (COALESCE(bp.YEAR_PAYMENT_AMOUNT,0)/COALESCE(bp.YEAR_INVOICE_AMOUNT,0))),2) yearlyPaidAmount,\n"
            + "	Round(((COALESCE(bp.YEAR_BILLED_AMOUNT,0) * (COALESCE(bp.YEAR_PAYMENT_AMOUNT,0)/COALESCE(bp.YEAR_INVOICE_AMOUNT,0))/COALESCE(bp.YEAR_BILLED_AMOUNT,0))*100),2) yearlyPaidPercentage\n"
            + "	from tblbpfreport bp\n"
            + "	left join tblcasesubcategoryid cs on cs.case_sub_category_id=bp.case_sub_category_id\n"
            + "	left join tblcasecategoryid cc on cc.case_category_id=bp.case_category_id ", nativeQuery = true)
    public List<BilledPaidFeesReportImpl> getBilledPaidFeesReport();

//    @Query(value = "select \n"
//            + "	bp.TK_CODE timekeeperCode, \n"
//            + "	bp.TK_NAME timekeeperName, \n"
//            + "	concat(bp.CASE_CATEGORY_ID,'-',cc.CASE_CATEGORY) caseCategory, \n"
//            + "	concat(bp.CASE_SUB_CATEGORY_ID,'-',cs.CASE_SUB_CATEGORY) caseSubCategory, \n"
//            + "	bp.MONTH_BILLED_AMOUNT monthlyBilledAmount, \n"
//            + "	bp.MONTH_APPROVED_AMOUNT monthlyTimeTicketAmount, \n"
//            + "	bp.YEAR_BILLED_AMOUNT yearlyBilledAmount, \n"
//            + "	bp.YEAR_APPROVED_AMOUNT yearlyTimeTicketAmount, \n"
//            + "	Round(((COALESCE(bp.MONTH_BILLED_AMOUNT,0)/COALESCE(bp.MONTH_APPROVED_AMOUNT,0))*100),2) monthlyBilledPercentage,\n"
//            + "	(COALESCE(bp.MONTH_BILLED_AMOUNT,0) * :mPaidAmount) monthlyPaidAmount,\n"
//            + "	Round((((COALESCE(bp.MONTH_BILLED_AMOUNT,0) * :mPaidAmount)/COALESCE(bp.MONTH_BILLED_AMOUNT,0))*100),2) monthlyPaidPercentage,\n"
//            + "	Round(((COALESCE(bp.YEAR_BILLED_AMOUNT,0)/COALESCE(bp.YEAR_APPROVED_AMOUNT,0))*100),2) yearlyBilledPercentage,\n"
//            + "	(COALESCE(bp.YEAR_BILLED_AMOUNT) * :yPaidAmount) yearlyPaidAmount,\n"
//            + "	Round(((COALESCE(bp.YEAR_BILLED_AMOUNT,0) * :yPaidAmount/COALESCE(bp.YEAR_BILLED_AMOUNT,0))*100),2) yearlyPaidPercentage\n"
//            + "	from tblbpfreport bp\n"
//            + "	left join tblcasesubcategoryid cs on cs.case_sub_category_id=bp.case_sub_category_id\n"
//            + "	left join tblcasecategoryid cc on cc.case_category_id=bp.case_category_id ", nativeQuery = true)
//    public List<BilledPaidFeesReportImpl> getBilledPaidFeesReport(@Param(value = "mPaidAmount") Double mPaidAmount,
//                                                                  @Param(value = "yPaidAmount") Double yPaidAmount);

}