package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.dto.ITimeTicket;
import com.mnrclara.api.accounting.model.prebill.ICurrentFees;
import com.mnrclara.api.accounting.model.prebill.MatterTimeTicket;
import com.mnrclara.api.accounting.model.prebill.outputform.IMatterTimeTicket;
import com.mnrclara.api.accounting.model.prebill.outputform.IMatterTimeTicketInvoice;

@Repository
@Transactional
public interface MatterTimeTicketRepository extends JpaRepository<MatterTimeTicket,Long>, JpaSpecificationExecutor<MatterTimeTicket>,DynamicNativeQuery {

	// AccountingService - PreBill Functionality
	public List<MatterTimeTicket> findByTimeTicketDateBetween(Date startDate, Date feesCutoff);
	public List<MatterTimeTicket> findByReferenceField1(String referenceField1);
	public List<MatterTimeTicket> findByTimeTicketNumber(String timeTicketNumber);
	
	@Query(value="SELECT SUM(APP_BILL_AMOUNT) AS BILLAMOUNT "
			+ "FROM tblmattertimeticketid \r\n"
			+ "WHERE REF_FIELD_1 = :preBillNumber \r\n"
			+ "AND UPPER(BILL_TYPE) <> UPPER('NON-BILLABLE') AND IS_DELETED = 0\r\n"
			+ "GROUP BY REF_FIELD_1", nativeQuery=true)
    public Double getTimeTicketBillAmount(@Param ("preBillNumber") String preBillNumber);
	
	// Query by matternumber and prebillnumber
	public List<MatterTimeTicket> findByMatterNumberAndReferenceField1OrderByTimeTicketDateAscTimeTicketNumberAsc(String matterNumber, String preBillNumber);
	
	public List<MatterTimeTicket> findByMatterNumberAndReferenceField1AndBillTypeNot(String matterNumber, String preBillNumber,String billType);
	
	public List<MatterTimeTicket> findByMatterNumberAndStatusIdAndDeletionIndicatorAndTimeTicketDateBetween(String matterNumber, 
			Long statusId, Long deletionIndicator, Date startDate, Date feesCutoffDate);
	
	public List<MatterTimeTicket> findByMatterNumberAndDeletionIndicator(String matterNumber, Long deletionIndicator);
	
	/*
	 * OutputForms
	 */
	@Query(value="SELECT SUM(tt.APP_BILL_TIME) AS timeTicketHours,\r\n"
			+ "SUM(tt.APP_BILL_AMOUNT) AS timeTicketAmount, \r\n"
			+ "SUM(tt.TIME_TICKET_HRS) AS ttHours, SUM(tt.TIME_TICKET_AMOUNT) AS ttAmount, \r\n"
			+ "tt.TK_CODE AS timeKeeperCode,tc.tk_name as timeKeeperName, tt.BILL_TYPE as billType \r\n"
			+ "FROM tblmattertimeticketid tt\r\n"
			+ "join tbltimekeepercodeid tc on tt.TK_CODE = tc.TK_CODE\r\n"
			+ "WHERE tt.MATTER_NO = :matterNumber AND tt.REF_FIELD_1 = :preBillNumber \r\n"
			+ "GROUP BY tt.TK_CODE,tc.tk_name, tt.BILL_TYPE", nativeQuery=true)
    public List<IMatterTimeTicket> getTimeKeeperSummary (@Param ("matterNumber") String matterNumber,@Param ("preBillNumber") String preBillNumber);
	
	@Query(value="SELECT SUM(APP_BILL_TIME) AS ApprovedBillableHours,\r\n"
			+ "SUM(APP_BILL_AMOUNT) AS approvedBillableAmount, TK_CODE AS timeKeeperCode, bill_Type AS billType\r\n"
			+ "FROM tblmattertimeticketid\r\n"
			+ "WHERE MATTER_NO = :matterNumber AND REF_FIELD_1 = :preBillNumber AND UCASE(bill_type) != UCASE('Non-Billable') \r\n"
			+ "GROUP BY TK_CODE, bill_type", nativeQuery=true)
    public List<IMatterTimeTicketInvoice> getTimeKeeperSummaryForInvoice (@Param ("matterNumber") String matterNumber,@Param ("preBillNumber") String preBillNumber);
	
	@Query(value="select SUM(TIME_TICKET_AMOUNT) AS fees \r\n"
			+ "FROM tblmattertimeticketid \r\n"
			+ "WHERE TIME_TICKET_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) \r\n"
			+ "AND MATTER_NO = :matterNumber AND STATUS_ID IN (34, 56) AND UCASE(BILL_TYPE)='BILLABLE'", nativeQuery=true)
    public List<Double> getAccountAgingDetails(@Param ("fromDiff") Long fromDiff, @Param ("toDiff") Long toDiff,
    		@Param ("matterNumber") String matterNumber);

	@Query(value="SELECT SUM(APP_BILL_AMOUNT) AS appBillAmount, SUM(TIME_TICKET_AMOUNT) AS timeTicketAmount \r\n"
			+ "	FROM tblmattertimeticketid \r\n"
			+ "	WHERE REF_FIELD_1 = :preBillNumber AND UCASE(BILL_TYPE)='BILLABLE' \r\n"
			+ "	GROUP BY REF_FIELD_1;", nativeQuery=true)
    public ICurrentFees getCurrentFees(@Param ("preBillNumber") String preBillNumber);
	
	/*
	 * SELECT SUM(APP_BILL_TIME) AS TOTALAMT FROM tblmattertimeticketid 
	 * WHERE MATTER_NO = '500020-04' AND REF_FIELD_1 = '701524-0023' 
	 * AND TIME_TICKET_DATE BETWEEN '2023-04-01 00:00:00' AND '2023-04-30 23:59:59' 
	 * AND CLASS_ID=1 AND status_id=51 GROUP BY REF_FIELD_1;
	 */
	@Query(value="SELECT SUM(APP_BILL_TIME) AS TOTALAMT "
			+ "FROM tblmattertimeticketid \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND REF_FIELD_1 = :preBillNumber \r\n"
			+ "AND TIME_TICKET_DATE BETWEEN :startTTD AND :endTTD AND IS_DELETED=0 "
			+ "GROUP BY REF_FIELD_1", nativeQuery=true)
    public Double getTotalHours(@Param ("preBillNumber") String preBillNumber, @Param ("matterNumber") String matterNumber,
    		@Param ("startTTD") Date startTTD, @Param ("endTTD") Date endTTD);

	@Query(value="SELECT ASSIGNED_RATE AS assignedRate \r\n"
			+ "	FROM tblmatterrateid \r\n"
			+ "	WHERE MATTER_NO = :matterNumber and TK_CODE = :tkCode ", nativeQuery=true)
	public Double getAssignedRateFromMatterRate(@Param ("matterNumber") String matterNumber, @Param ("tkCode") String tkCode);
	
	public List<MatterTimeTicket> findByMatterNumberAndReferenceField1AndBillTypeNotOrderByTimeTicketDateAscTimeTicketNumberAsc(
			String matterNumber, String preBillNumber, String string);
	
	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblmattertimeticketid set STATUS_ID = 59, IS_DELETED = 1 \r\n"
			+ " WHERE TIME_TICKET_NO = :timeTicketNumber", nativeQuery = true)
	public void deleteMatterTimeTicket(@Param(value = "timeTicketNumber") String timeTicketNumber);
	
	//-----------------------------AR REPORT---------------------------------------------------------------
	@Query(value="SELECT DISTINCT MATTER_NO FROM tblmattergenaccid \r\n"
			+ "	WHERE MATTER_NO IN (SELECT MATTER_NO FROM tblmattertimeticketid \r\n"
			+ "					WHERE TK_CODE IN :tkCode AND STATUS_ID = 51 AND IS_DELETED = 0 AND CLASS_ID IN :classID) \r\n"
			+ "	AND STATUS_ID <> 30 AND IS_DELETED = 0 AND CLASS_ID IN :classID", nativeQuery=true)
	public List<String> getMatterNumbersByTKCode(@Param (value = "tkCode") List<String> tkCode, @Param (value = "classID") List<Long> classID);
	
	@Query(value="SELECT DISTINCT MATTER_NO FROM tblmattergenaccid \r\n"
			+ "	WHERE IS_DELETED = 0 AND CLASS_ID IN :classID", nativeQuery=true)
	public List<String> getMatterNumbers(@Param (value = "classID") List<Long> classID);

	//--------------------------------User Type Description--------------------------------------------------

	//get UserTypeIdDescription
	@Query(value = "SELECT USR_TYP_TEXT FROM tblusertypeid WHERE USR_TYP_ID in (:userTypeId) \r\n"
			+ "and is_deleted = 0", nativeQuery = true)
	public String getUserTypeIdDescription(@Param("userTypeId") Long userTypeId);
	
	public List<MatterTimeTicket> findByMatterNumberAndReferenceField1AndBillTypeNotAndApprovedBillableAmountGreaterThanOrderByTimeTicketDateAscTimeTicketNumberAsc(
			String matterNumber, String preBillNumber, String string, double d);
}