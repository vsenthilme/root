package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mnrclara.api.management.model.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;

@Repository
@Transactional
public interface MatterTimeTicketRepository extends JpaRepository<MatterTimeTicket, Long>, JpaSpecificationExecutor<MatterTimeTicket>, DynamicNativeQueryTT {

    public List<MatterTimeTicket> findAll();

    public Optional<MatterTimeTicket> findByTimeTicketNumber(String timeTicketNumber);

    public List<MatterTimeTicket> findByReferenceField1AndDeletionIndicator(String referenceField1, Long deletionIndicator);

    //--------------INDIVIDUAL---QUERY---------------------------------------------------------------------------------------------
    @Query(value = "SELECT COUNT(TIME_TICKET_NO) AS timeTicketCount, SUM(TIME_TICKET_AMOUNT) AS timeTicketAmount, \r\n"
            + "MATTER_NO AS matterNumber "
            + "FROM tblmattertimeticketid \r\n"
            + "WHERE MATTER_NO IN :matterNumber "
            + "AND TIME_TICKET_DATE BETWEEN :startDate AND :feesCutoffDate "
            + "AND STATUS_ID = 33 AND BILL_TYPE = 'Billable' AND IS_DELETED = 0\r\n"
            + "GROUP BY MATTER_NO",
            nativeQuery = true)
    List<IMatterTimeTicketCountAndSum> findCountAndSumOfTimeTicketsByIndividual(@Param("matterNumber") List<String> matterNumber,
                                                                                @Param("startDate") Date startDate,
                                                                                @Param("feesCutoffDate") Date feesCutoffDate);

    //--------------INDIVIDUAL--SELECT---QUERY-----------------------------------------------------------------------------------
    @Query(value = "SELECT TIME_TICKET_NO AS timeTicketNumber,\r\n"
            + "	LANG_ID AS languageId,\r\n"
            + "	CLASS_ID As classId,\r\n"
            + "	MATTER_NO AS matterNumber,\r\n"
            + "	CLIENT_ID AS clientId,\r\n"
            + "	TK_CODE AS timeKeeperCode,\r\n"
            + "	CASE_CATEGORY_ID AS caseCategoryId,\r\n"
            + "	CASE_SUB_CATEGORY_ID AS caseSubCategoryId,\r\n"
            + "	TIME_TICKET_HRS AS timeTicketHours,\r\n"
            + "	TIME_TICKET_DATE AS timeTicketDate,\r\n"
            + "	ACTIVITY_CODE AS activityCode,\r\n"
            + "	TASK_CODE AS taskCode,\r\n"
            + "	DEF_RATE AS defaultRate,\r\n"
            + "	RATE_UNIT AS rateUnit,\r\n"
            + "	TIME_TICKET_AMOUNT AS timeTicketAmount,\r\n"
            + "	BILL_TYPE AS billType,\r\n"
            + "	TIME_TICKET_TEXT AS timeTicketDescription,\r\n"
            + "	ASS_PARTNER AS assignedPartner,\r\n"
            + "	ASS_ON AS assignedOn,\r\n"
            + "	APP_BILL_TIME AS approvedBillableTimeInHours,\r\n"
            + "	APP_BILL_AMOUNT AS approvedBillableAmount,\r\n"
            + "	APP_ON AS approvedOn,\r\n"
            + "	STATUS_ID AS statusId,\r\n"
            + "	CTD_BY AS createdBy,\r\n"
            + "	CTD_ON AS createdOn,\r\n"
            + "	UTD_BY AS updatedBy,\r\n"
            + "	UTD_ON AS updatedOn\r\n"
            + " FROM tblmattertimeticketid \r\n"
            + " WHERE MATTER_NO IN :matterNumber \r\n"
            + " AND TIME_TICKET_DATE BETWEEN :startDate AND :feesCutoffDate AND IS_DELETED = 0\r\n "
            + " AND STATUS_ID = 33",
            nativeQuery = true)
    List<IMatterTimeTicket> findTimeTicketsByIndividual(@Param("matterNumber") List<String> matterNumber,
                                                        @Param("startDate") Date startDate,
                                                        @Param("feesCutoffDate") Date feesCutoffDate);

    /*----------------GROUP-------------------------------------------------------------------------------------------------------*/
    @Query(value = "SELECT COUNT(TIME_TICKET_NO) AS timeTicketCount, \r\n"
            + "	SUM(TIME_TICKET_AMOUNT) AS timeTicketAmount,"
            + " tblmattertimeticketid.MATTER_NO AS matterNumber \r\n"
            + "	FROM tblmattertimeticketid INNER JOIN tblmatterassignmentid\r\n"
            + "	ON tblmattertimeticketid.MATTER_NO = tblmatterassignmentid.MATTER_NO \r\n"
            + "	WHERE tblmattertimeticketid.MATTER_NO IN :matterNumber \r\n"
            + "	AND TIME_TICKET_DATE BETWEEN :startDate AND :feesCutoffDate \r\n"
            + "	AND tblmattertimeticketid.STATUS_ID = 33 AND tblmattertimeticketid.bill_type = 'Billable' \r\n"
            + " AND tblmattertimeticketid.IS_DELETED = 0\r\n"
            + "	OR ORIGINATING_TK IN :originatingTimeKeeper \r\n"
            + " OR RESPONSIBLE_TK IN :responsibleTimeKeeper "
            + "	OR ASSIGNED_TK IN :assignedTimeKeeper \r\n"
            + "    GROUP BY tblmattertimeticketid.MATTER_NO",
            nativeQuery = true)
    List<IMatterTimeTicketCountAndSum> findCountAndSumOfTimeTicketsByGroup(@Param("matterNumber") List<String> matterNumber,
                                                                           @Param("startDate") Date startDate,
                                                                           @Param("feesCutoffDate") Date feesCutoffDate,
                                                                           @Param("originatingTimeKeeper") List<String> originatingTimeKeeper,
                                                                           @Param("responsibleTimeKeeper") List<String> responsibleTimeKeeper,
                                                                           @Param("assignedTimeKeeper") List<String> assignedTimeKeeper);

    //--------------GROUP--SELECT---QUERY-----------------------------------------------------------------------------------
    @Query(value = "SELECT tblmattertimeticketid.TIME_TICKET_NO AS timeTicketNumber,\r\n"
            + "tblmattertimeticketid.LANG_ID AS languageId,\r\n"
            + "tblmattertimeticketid.CLASS_ID As classId,\r\n"
            + "tblmattertimeticketid.MATTER_NO AS matterNumber,\r\n"
            + "tblmattertimeticketid.CLIENT_ID AS clientId,\r\n"
            + "tblmattertimeticketid.TK_CODE AS timeKeeperCode,\r\n"
            + "tblmattertimeticketid.CASE_CATEGORY_ID AS caseCategoryId,\r\n"
            + "tblmattertimeticketid.CASE_SUB_CATEGORY_ID AS caseSubCategoryId,\r\n"
            + "tblmattertimeticketid.TIME_TICKET_HRS AS timeTicketHours,\r\n"
            + "tblmattertimeticketid.TIME_TICKET_DATE AS timeTicketDate,\r\n"
            + "tblmattertimeticketid.ACTIVITY_CODE AS activityCode,\r\n"
            + "tblmattertimeticketid.TASK_CODE AS taskCode,\r\n"
            + "tblmattertimeticketid.DEF_RATE AS defaultRate,\r\n"
            + "tblmattertimeticketid.RATE_UNIT AS rateUnit,\r\n"
            + "tblmattertimeticketid.TIME_TICKET_AMOUNT AS timeTicketAmount,\r\n"
            + "tblmattertimeticketid.BILL_TYPE AS billType,\r\n"
            + "tblmattertimeticketid.TIME_TICKET_TEXT AS timeTicketDescription,\r\n"
            + "tblmattertimeticketid.ASS_PARTNER AS assignedPartner,\r\n"
            + "tblmattertimeticketid.ASS_ON AS assignedOn,\r\n"
            + "tblmattertimeticketid.APP_BILL_TIME AS approvedBillableTimeInHours,\r\n"
            + "tblmattertimeticketid.APP_BILL_AMOUNT AS approvedBillableAmount,\r\n"
            + "tblmattertimeticketid.APP_ON AS approvedOn,\r\n"
            + "tblmattertimeticketid.STATUS_ID AS statusId,\r\n"
            + "tblmattertimeticketid.CTD_BY AS createdBy,\r\n"
            + "tblmattertimeticketid.CTD_ON AS createdOn,\r\n"
            + "tblmattertimeticketid.UTD_BY AS updatedBy,\r\n"
            + "tblmattertimeticketid.UTD_ON AS updatedOn\r\n"
            + "FROM tblmattertimeticketid INNER JOIN tblmatterassignmentid\r\n"
            + "ON tblmattertimeticketid.MATTER_NO = tblmatterassignmentid.MATTER_NO \r\n"
            + "WHERE tblmattertimeticketid.MATTER_NO IN :matterNumber \r\n"
            + "AND tblmattertimeticketid.TIME_TICKET_DATE BETWEEN :startDate AND :feesCutoffDate \r\n"
            + "AND tblmattertimeticketid.STATUS_ID = 33 AND tblmattertimeticketid.IS_DELETED = 0",
            nativeQuery = true)
    List<IMatterTimeTicket> findTimeTicketsByGroup(
            @Param("matterNumber") List<String> matterNumber,
            @Param("startDate") Date startDate,
            @Param("feesCutoffDate") Date feesCutoffDate);

    //-----------------------------------------------------------------------------------------------------------------------
//	@Query(value="select MATTER_NO AS matterNumber, SUM(TIME_TICKET_AMOUNT) AS fees, SUM(TIME_TICKET_HRS) AS timeTicketHours \r\n"
//			+ "FROM tblmattertimeticketid \r\n"
//			+ "WHERE TIME_TICKET_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) \r\n"
//			+ "AND MATTER_NO IN (:matterNumber) AND STATUS_ID = 33 AND UCASE(BILL_TYPE)='BILLABLE' AND IS_DELETED = 0 \r\n"
//			+ "GROUP BY matter_no  \r\n"
//			+ "HAVING SUM(TIME_TICKET_AMOUNT) > 0 ", nativeQuery=true)
//    public List<ITimeTicket> getAccountAgingDetails(
//    		@Param ("fromDiff") Long fromDiff, 
//    		@Param ("toDiff") Long toDiff,
//    		@Param ("matterNumber") Set<String> matterNumberList);

    @Query(value = "select MATTER_NO AS matterNumber, SUM(TIME_TICKET_AMOUNT) AS fees, SUM(TIME_TICKET_HRS) AS timeTicketHours \r\n"
            + "FROM tblmattertimeticketid \r\n"
            + "WHERE TIME_TICKET_DATE BETWEEN :fromDiff AND :toDiff \r\n"
            + "AND MATTER_NO IN (:matterNumber) AND STATUS_ID = 33 AND UCASE(BILL_TYPE)='BILLABLE' AND IS_DELETED = 0 \r\n"
            + "GROUP BY matter_no  \r\n"
            + "HAVING SUM(TIME_TICKET_AMOUNT) > 0 ", nativeQuery = true)
    public List<ITimeTicket> getAccountAgingDetails(
            @Param("fromDiff") Date fromDiff,
            @Param("toDiff") Date toDiff,
            @Param("matterNumber") Set<String> matterNumberList);

    @Query(value = "SELECT SUM(APP_BILL_AMOUNT) AS approvedBillableAmount\r\n"
            + "FROM tblmattertimeticketid \r\n"
            + "WHERE REF_FIELD_1 = :preBillNumber \r\n"
            + "AND is_deleted = 0 ", nativeQuery = true)
    public Double findApprovedBillableAmountByPreBillNumber(@Param("preBillNumber") String preBillNumber);

    @Query(value = "SELECT SUM(TIME_TICKET_AMOUNT) AS approvedBillableAmount\r\n"
            + "FROM tblmattertimeticketid \r\n"
            + "WHERE REF_FIELD_1 = :preBillNumber \r\n"
            + "AND is_deleted = 0 ", nativeQuery = true)
    public Double findTimTicketAmountByPreBillNumber(@Param("preBillNumber") String preBillNumber);

    //Approve Prebill
    @Query(value = "SELECT \r\n" +
            "mt.TIME_TICKET_NO	timeTicketNumber, \r\n" +
            "mt.LANG_ID	languageId, \r\n" +
            "mt.CLASS_ID	classId, \r\n" +
            "mt.MATTER_NO	matterNumber, \r\n" +
            "mt.CLIENT_ID	clientId, \r\n" +
            "mt.TK_CODE	timeKeeperCode, \r\n" +
            "mt.CASE_CATEGORY_ID	caseCategoryId, \r\n" +
            "mt.CASE_SUB_CATEGORY_ID	caseSubCategoryId, \r\n" +
            "mt.TIME_TICKET_HRS	timeTicketHours, \r\n" +
            "mt.TIME_TICKET_DATE	timeTicketDate, \r\n" +
            "mt.ACTIVITY_CODE	activityCode, \r\n" +
            "mt.TASK_CODE	taskCode, \r\n" +
            "mt.DEF_RATE	defaultRate, \r\n" +
            "mt.RATE_UNIT	rateUnit, \r\n" +
            "mt.TIME_TICKET_AMOUNT	timeTicketAmount, \r\n" +
            "mt.BILL_TYPE	billType, \r\n" +
            "mt.TIME_TICKET_TEXT	timeTicketDescription, \r\n" +
            "mt.ASS_PARTNER	assignedPartner, \r\n" +
            "mt.ASS_ON	assignedOn, \r\n" +
            "mt.APP_BILL_TIME	approvedBillableTimeInHours, \r\n" +
            "mt.APP_BILL_AMOUNT	approvedBillableAmount, \r\n" +
            "mt.APP_ON	approvedOn, \r\n" +
            "mt.STATUS_ID	statusId, \r\n" +
            "mt.IS_DELETED	deletionIndicator, \r\n" +
            "mt.REF_FIELD_1	referenceField1, \r\n" +
            "mt.REF_FIELD_2	referenceField2, \r\n" +
            "mt.REF_FIELD_3	referenceField3, \r\n" +
            "mt.REF_FIELD_4	referenceField4, \r\n" +
            "mt.REF_FIELD_5	referenceField5, \r\n" +
            "mt.REF_FIELD_6	referenceField6, \r\n" +
            "mt.REF_FIELD_7	referenceField7, \r\n" +
            "mt.REF_FIELD_8	referenceField8, \r\n" +
            "mt.REF_FIELD_9	referenceField9, \r\n" +
            "mt.REF_FIELD_10	referenceField10, \r\n" +
            "mt.CTD_BY	createdBy, \r\n" +
            "mt.CTD_ON	createdOn, \r\n" +
            "mt.UTD_BY	updatedBy, \r\n" +
            "mt.UTD_ON	updatedOn, \r\n" +
            "mr.ASSIGNED_RATE	assignedRatePerHour \r\n" +
            "FROM tblmattertimeticketid mt, tblmatterrateid mr \r\n" +
            "WHERE mt.matter_no = mr.matter_no and mt.tk_code = mr.tk_code and mt.IS_DELETED = 0 and \r\n" +
            "mt.REF_FIELD_1 in (:preBillNumber) ORDER BY mt.tk_code;", nativeQuery = true)
    public List<IMatterTimeTicket> approvePreBill(@Param("preBillNumber") String preBillNumber);

    //Billable Hours by TimeKeeper Report
    @Query(value = "select \n"
            + "tk_code timeKeeperCode, \n"
            + "tk_name timeKeeperName, \n"
            + "def_rate defaultRate, \n"
            + "usr_typ_id userType \n"
            + "from tbltimekeepercodeid tk \n"
            + "where \n"
            + "(COALESCE(:timeKeeperCode,null) IS NULL OR (tk.tk_code IN (:timeKeeperCode))) and \n"
			+ "(COALESCE(:classId,null) IS NULL OR (tk.class_id IN (:classId))) and \n"
            + "tk.is_deleted = 0", nativeQuery = true)
    public List<ITimeKeeperDetail> findTimeKeeper(@Param("timeKeeperCode") List<String> timeKeeperCode,
												   @Param ("classId") Long classId);


    //Billable
    @Query(value = "select \n"
            + "sum(time_ticket_hrs) billableHours \n"
            + "from tblmattertimeticketid tt \n"
            + "where \n"
            + "tt.tk_code IN (:timeKeeperCode) and tt.class_id IN (:classId) and \n"
            + "(COALESCE(:startDate,null) IS NULL OR (tt.time_ticket_date BETWEEN :startDate AND :endDate)) and \n"
            + "tt.is_deleted=0 and tt.bill_type in ('Billable')", nativeQuery = true)
    public Double findTimeKeeperTimeTicketHoursBillable(@Param("timeKeeperCode") String timeKeeperCode,
                                                        @Param("classId") Long classId,
                                                        @Param(value = "startDate") Date startDate,
                                                        @Param(value = "endDate") Date endDate);

    //Billed-Hours
    @Query(value = "select \n"
            + "sum(app_bill_time) billedHours \n"
            + "from tblmattertimeticketid tt \n"
            + "where \n"
            + "tt.tk_code IN (:timeKeeperCode) and tt.class_id IN (:classId) and \n"
            + "(COALESCE(:startDate,null) IS NULL OR (tt.time_ticket_date BETWEEN :startDate AND :endDate)) and \n"
            + "tt.status_id=51 and tt.is_deleted=0 and tt.bill_type in ('Billable')", nativeQuery = true)
    public Double findTimeKeeperTimeTicketHoursBilled(@Param("timeKeeperCode") String timeKeeperCode,
                                                      @Param("classId") Long classId,
                                                      @Param(value = "startDate") Date startDate,
                                                      @Param(value = "endDate") Date endDate);

    //Billed-Amount
    @Query(value = "select \n"
            + "sum(app_bill_amount) billedFees \n"
            + "from tblmattertimeticketid tt \n"
            + "where \n"
            + "tt.tk_code IN (:timeKeeperCode) and tt.class_id IN (:classId) and \n"
            + "(COALESCE(:startDate,null) IS NULL OR (tt.time_ticket_date BETWEEN :startDate AND :endDate)) and \n"
            + "tt.status_id=51 and tt.is_deleted=0 and tt.bill_type in ('Billable')", nativeQuery = true)
    public Double findTimeKeeperTimeTicketBilledAmount(@Param("timeKeeperCode") String timeKeeperCode,
                                                       @Param("classId") Long classId,
                                                       @Param(value = "startDate") Date startDate,
                                                       @Param(value = "endDate") Date endDate);

    //Non Billable
    @Query(value = "select \n"
            + "sum(time_ticket_hrs) nonBillableHours, \n"
            + "sum(app_bill_time) billedHours, \n"
            + "sum(app_bill_amount) billedFees \n"
            + "from tblmattertimeticketid tt \n"
            + "where \n"
            + "tt.tk_code IN (:timeKeeperCode) and tt.class_id IN (:classId) and \n"
            + "(COALESCE(:startDate,null) IS NULL OR (tt.time_ticket_date BETWEEN :startDate AND :endDate)) and \n"
            + "tt.is_deleted=0 and tt.bill_type in ('Non-Billable')", nativeQuery = true)
    public ITimeKeeperBillableHoursReport findTimeKeeperTimeTicketHoursNonBillable(@Param("timeKeeperCode") String timeKeeperCode,
                                                                                   @Param("classId") Long classId,
                                                                                   @Param(value = "startDate") Date startDate,
                                                                                   @Param(value = "endDate") Date endDate);

    //----------------------------------------------------------------------------------------------------------------------------------
    
    @Query(value = "SELECT CLASS_ID AS classId, TK_CODE as timeKeeperCode, SUM(TIME_TICKET_AMOUNT) AS timeTicketAmount, SUM(TIME_TICKET_HRS) AS timeTicketHours \r\n"
    		+ "FROM tblmattertimeticketid \r\n"
    		+ "WHERE TIME_TICKET_DATE BETWEEN :fromDiff AND :toDiff AND IS_DELETED = 0 \r\n"
    		+ "GROUP BY TK_CODE, CLASS_ID", nativeQuery = true)
    public List<ITimeTicketNotification> getTimeTicketDetails (@Param("fromDiff") Date fromDiff, @Param("toDiff") Date toDiff);
    
    @Query(value = "SELECT CLASS_ID AS classId, TK_CODE as timeKeeperCode, SUM(TIME_TICKET_AMOUNT) AS timeTicketAmount, SUM(TIME_TICKET_HRS) AS timeTicketHours \r\n"
    		+ "FROM tblmattertimeticketid \r\n"
    		+ "WHERE CTD_ON BETWEEN :fromDiff AND :toDiff AND IS_DELETED = 0 \r\n"
    		+ "GROUP BY TK_CODE, CLASS_ID", nativeQuery = true)
    public List<ITimeTicketNotification> getTimeTicketDetailsByFirstDayOfWeek (@Param("fromDiff") Date fromDiff, @Param("toDiff") Date toDiff);

}