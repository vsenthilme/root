package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.mnrclara.api.management.model.dto.*;
import com.mnrclara.api.management.model.dto.IMatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.*;
import com.mnrclara.api.management.repository.MatterRateRepository;
import com.mnrclara.api.management.repository.specification.TimeTicketNotificationSpecification;
import com.mongodb.connection.Stream;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.mattertimeticket.AddMatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.SearchMatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.TimeTicketNotification;
import com.mnrclara.api.management.model.mattertimeticket.UpdateMatterTimeTicket;
import com.mnrclara.api.management.repository.MatterTimeTicketRepository;
import com.mnrclara.api.management.repository.TimeTicketNotificationRepository;
import com.mnrclara.api.management.repository.specification.MatterTimeTicketSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterTimeTicketService {

    private static final String MATTERTIMETICKET = "MATTERTIMETICKET";
    private static final Long CLASSID = 1L;

    @Autowired
    private MatterTimeTicketRepository matterTimeTicketRepository;
    
    @Autowired
    private TimeTicketNotificationRepository timeTicketNotificationRepository;

    @Autowired
    private MatterGenAccService matterGenAccService;

    @Autowired
    private SetupService setupService;

    @Autowired
    private AuthTokenService authTokenService;

    /**
     * getMatterTimeTickets
     *
     * @return
     */
    public List<MatterTimeTicket> getMatterTimeTickets() {
        List<MatterTimeTicket> matterTimeTicketList = matterTimeTicketRepository.findAll();
        matterTimeTicketList =
                matterTimeTicketList.stream()
                        .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                        .collect(Collectors.toList());
        return matterTimeTicketList;
    }

    /**
     * getMatterTimeTicket
     *
     * @param timeTicketNumber
     * @return
     */
    public MatterTimeTicket getMatterTimeTicket(String timeTicketNumber) {
        MatterTimeTicket matterTimeTicket = matterTimeTicketRepository.findByTimeTicketNumber(timeTicketNumber).orElse(null);
        if (matterTimeTicket != null &&
                matterTimeTicket.getDeletionIndicator() != null && matterTimeTicket.getDeletionIndicator() == 0) {
            return matterTimeTicket;
        } else {
            throw new BadRequestException("The given MatterTimeTicket ID : " + timeTicketNumber + " doesn't exist.");
        }
    }

    /**
     * @param preBillNumber
     * @return
     */
    public List<IMatterTimeTicket> getMatterTimeTicketForApprove(String preBillNumber) {
        List<IMatterTimeTicket> matterTimeTicket = matterTimeTicketRepository.approvePreBill(preBillNumber);
        if (matterTimeTicket != null && !matterTimeTicket.isEmpty()) {
            return matterTimeTicket;
        }
        return null;
    }

    /**
     * findMatterTimeTicket
     *
     * @param searchMatterTimeTicket
     * @return
     * @throws ParseException
     */
    public List<MatterTimeTicket> findMatterTimeTicket(SearchMatterTimeTicket searchMatterTimeTicket) throws ParseException {
        if (searchMatterTimeTicket.getStartCreatedOn() != null && searchMatterTimeTicket.getEndCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTimeTicket.getStartCreatedOn(), searchMatterTimeTicket.getEndCreatedOn());
            searchMatterTimeTicket.setStartCreatedOn(dates[0]);
            searchMatterTimeTicket.setEndCreatedOn(dates[1]);
        }

        if (searchMatterTimeTicket.getStartTimeTicketDate() != null && searchMatterTimeTicket.getEndTimeTicketDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTimeTicket.getStartTimeTicketDate(),
                    searchMatterTimeTicket.getEndTimeTicketDate());
            searchMatterTimeTicket.setStartTimeTicketDate(dates[0]);
            searchMatterTimeTicket.setEndTimeTicketDate(dates[1]);
        }

        MatterTimeTicketSpecification spec = new MatterTimeTicketSpecification(searchMatterTimeTicket);
        List<MatterTimeTicket> searchResults = matterTimeTicketRepository.findAll(spec);
        log.info("searchResults: " + searchResults);
        return searchResults;
    }

    /**
     * createMatterTimeTicket
     *
     * @param newMatterTimeTicket
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public MatterTimeTicket createMatterTimeTicket(AddMatterTimeTicket newMatterTimeTicket, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        MatterTimeTicket dbMatterTimeTicket = new MatterTimeTicket();
        BeanUtils.copyProperties(newMatterTimeTicket, dbMatterTimeTicket, CommonUtils.getNullPropertyNames(newMatterTimeTicket));

        // TK_CODE
        if (newMatterTimeTicket.getTimeKeeperCode() != null) {

            dbMatterTimeTicket.setTimeKeeperCode(newMatterTimeTicket.getTimeKeeperCode()); //Time Ticket Create in Prebill screen

        } else {

            dbMatterTimeTicket.setTimeKeeperCode(loginUserID);

        }

        // TIME_TICKET_AMOUNT
        // Calculated field - DEF_RATE * TIME_TICKET_HRS
        Double timeTicketAmount = newMatterTimeTicket.getDefaultRate() * newMatterTimeTicket.getTimeTicketHours();
        dbMatterTimeTicket.setTimeTicketAmount(timeTicketAmount);

        MatterGenAcc dbMatterGenAcc = matterGenAccService.getMatterGenAcc(newMatterTimeTicket.getMatterNumber());

        // LANG_ID
        dbMatterTimeTicket.setLanguageId(dbMatterGenAcc.getLanguageId());

        // CLASS_ID
        dbMatterTimeTicket.setClassId(dbMatterGenAcc.getClassId());

        // CLIENT_ID
        dbMatterTimeTicket.setClientId(dbMatterGenAcc.getClientId());

        /*
         * TIMETICKET_NO
         * ------------------
         * During Save, check the CLASS_ID values
         * 1. If CLASS_ID=1, then pass CLASS_ID, NUM_RAN_CODE=12 in NUMBERRANGE table and
         * Fetch NUM_RAN_CURRENT values and add +1 and then insert
         * 2. If CLASS_ID=2, then pass CLASS_ID, NUM_RAN_CODE=13 in NUMBERRANGE table and
         * Fetch NUM_RAN_CURRENT values and add +1 and then insert
         */
        long classID = dbMatterGenAcc.getClassId().longValue();
        long NUM_RAN_CODE = 0;
        if (classID == 1) {
            NUM_RAN_CODE = 12;
        } else if (classID == 2) {
            NUM_RAN_CODE = 13;
        }

        // Get AuthToken for SetupService
        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
        String newTimeTicketNumber = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
                authTokenForSetupService.getAccess_token());
        log.info("nextVal from NumberRange for newTimeTicketNumber: " + newTimeTicketNumber);
        dbMatterTimeTicket.setTimeTicketNumber(newTimeTicketNumber);

        // Time Ticket Date --- Adding Time
//		Date dateWithTime = DateUtils.addCurrentTimeToDate (dbMatterTimeTicket.getTimeTicketDate());
//		dbMatterTimeTicket.setTimeTicketDate(dateWithTime);

        // STATUS_ID
        dbMatterTimeTicket.setStatusId(33L);

        // CTD_BY - logged in USR_ID
        dbMatterTimeTicket.setCreatedBy(loginUserID);

        // CTD_ON - Server time
        dbMatterTimeTicket.setCreatedOn(new Date());

        // UTD_BY - logged in USR_ID
        dbMatterTimeTicket.setUpdatedBy(loginUserID);

        // UTD_ON - Server time
        dbMatterTimeTicket.setUpdatedOn(new Date());

        dbMatterTimeTicket.setDeletionIndicator(0L);
        log.info("dbMatterTimeTicket : " + dbMatterTimeTicket);

        return matterTimeTicketRepository.save(dbMatterTimeTicket);
    }

    /**
     * @param newMatterTimeTickets
     * @param loginUserID
     */
    public void createBulkMatterTimeTickets(@Valid AddMatterTimeTicket[] newMatterTimeTickets, String loginUserID) {
        List<MatterTimeTicket> createMatterTimeTickets = new ArrayList<>();
        for (AddMatterTimeTicket newMatterTimeTicket : newMatterTimeTickets) {
            MatterTimeTicket dbMatterTimeTicket = new MatterTimeTicket();
            BeanUtils.copyProperties(newMatterTimeTicket, dbMatterTimeTicket, CommonUtils.getNullPropertyNames(newMatterTimeTicket));
            dbMatterTimeTicket.setTimeKeeperCode(loginUserID);
            // STATUS_ID
            dbMatterTimeTicket.setStatusId(33L);

            dbMatterTimeTicket.setDeletionIndicator(0L);
            createMatterTimeTickets.add(dbMatterTimeTicket);
        }
        List<MatterTimeTicket> createdMatterTimeTickets =
                matterTimeTicketRepository.saveAll(createMatterTimeTickets);
        log.info("createdMatterTimeTickets : " + createdMatterTimeTickets);
    }

    /**
     * updateMatterTimeTicket
     *
     * @param timeTicketNumber
     * @param updateMatterTimeTicket
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public MatterTimeTicket updateMatterTimeTicket(String timeTicketNumber, UpdateMatterTimeTicket updateMatterTimeTicket, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        MatterTimeTicket dbMatterTimeTicket = getMatterTimeTicket(timeTicketNumber);
        BeanUtils.copyProperties(updateMatterTimeTicket, dbMatterTimeTicket, CommonUtils.getNullPropertyNames(updateMatterTimeTicket));

        // Get AuthToken for SetupService
        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

        // TIME_TICKET_HRS
        if (updateMatterTimeTicket.getTimeTicketHours() != null
                && updateMatterTimeTicket.getTimeTicketHours().doubleValue() == dbMatterTimeTicket.getTimeTicketHours().doubleValue()) {
            log.info("Inserting Audit log for TIME_TICKET_HRS");
            setupService.createAuditLogRecord(loginUserID,
                    String.valueOf(timeTicketNumber),
                    3L,
                    MATTERTIMETICKET,
                    "TIME_TICKET_HRS",
                    String.valueOf(dbMatterTimeTicket.getTimeTicketHours()),
                    String.valueOf(updateMatterTimeTicket.getTimeTicketHours()),
                    authTokenForSetupService.getAccess_token());
        }

        // ACTIVITY_CODE
        if (updateMatterTimeTicket.getActivityCode() != null
                && !updateMatterTimeTicket.getActivityCode().equalsIgnoreCase(dbMatterTimeTicket.getActivityCode())) {
            log.info("Inserting Audit log for ACTIVITY_CODE");
            setupService.createAuditLogRecord(loginUserID,
                    String.valueOf(timeTicketNumber),
                    3L,
                    MATTERTIMETICKET,
                    "ACTIVITY_CODE",
                    dbMatterTimeTicket.getActivityCode(),
                    updateMatterTimeTicket.getActivityCode(),
                    authTokenForSetupService.getAccess_token());
        }

        // TASK_CODE
        if (updateMatterTimeTicket.getTaskCode() != null
                && !updateMatterTimeTicket.getTaskCode().equalsIgnoreCase(dbMatterTimeTicket.getTaskCode())) {
            log.info("Inserting Audit log for TASK_CODE");
            setupService.createAuditLogRecord(loginUserID,
                    String.valueOf(timeTicketNumber),
                    3L,
                    MATTERTIMETICKET,
                    "TASK_CODE",
                    dbMatterTimeTicket.getTaskCode(),
                    updateMatterTimeTicket.getTaskCode(),
                    authTokenForSetupService.getAccess_token());
        }

        // BILL_TYPE
        if (updateMatterTimeTicket.getBillType() != null
                && !updateMatterTimeTicket.getBillType().equalsIgnoreCase(dbMatterTimeTicket.getBillType())) {
            log.info("Inserting Audit log for BILL_TYPE");
            setupService.createAuditLogRecord(loginUserID,
                    String.valueOf(timeTicketNumber),
                    3L,
                    MATTERTIMETICKET,
                    "BILL_TYPE",
                    dbMatterTimeTicket.getBillType(),
                    updateMatterTimeTicket.getBillType(),
                    authTokenForSetupService.getAccess_token());
        }

        // UTD_BY - logged in USR_ID
        dbMatterTimeTicket.setUpdatedBy(loginUserID);

        // UTD_ON - Server time
        dbMatterTimeTicket.setUpdatedOn(new Date());
        return matterTimeTicketRepository.save(dbMatterTimeTicket);
    }

    /**
     * deleteMatterTimeTicket
     *
     * @param timeTicketNumber
     * @param loginUserID
     */
    public void deleteMatterTimeTicket(String timeTicketNumber, String loginUserID) {
        MatterTimeTicket matterTimeticket = getMatterTimeTicket(timeTicketNumber);
        if (matterTimeticket != null && matterTimeticket.getStatusId() == 33L) {
            matterTimeticket.setDeletionIndicator(1L);
            matterTimeticket.setReferenceField10("Matter Time Ticket Deleted");
            matterTimeTicketRepository.save(matterTimeticket);
        } else {
            throw new EntityNotFoundException("Selected Task Can't be deleted as it has been already closed");
        }
    }

    //TimeKeeper Billing Report
    public List<TimeKeeperBillingReport> findTimeKeeperBillingReport(TimeKeeperBillingReportInput timeKeeperBillingReportInput) throws ParseException {

        if (timeKeeperBillingReportInput.getStartDate() != null && timeKeeperBillingReportInput.getEndDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(timeKeeperBillingReportInput.getStartDate(), timeKeeperBillingReportInput.getEndDate());
            timeKeeperBillingReportInput.setStartDate(dates[0]);
            timeKeeperBillingReportInput.setEndDate(dates[1]);
        }

        if (timeKeeperBillingReportInput.getClassId() == null || timeKeeperBillingReportInput.getClassId().isEmpty()) {
            timeKeeperBillingReportInput.setClassId(null);
        }

        if (timeKeeperBillingReportInput.getTimekeeperCode() == null || timeKeeperBillingReportInput.getTimekeeperCode().isEmpty()) {
            timeKeeperBillingReportInput.setTimekeeperCode(null);
        }

        List<ITimeKeeperDetail> timeKeeperDetails = matterTimeTicketRepository.findTimeKeeper(
                timeKeeperBillingReportInput.getTimekeeperCode(), CLASSID );        //Class ID Hard code to 1 since it is L&E Report

        List<TimeKeeperBillingReport> timeKeeperBillingReportList = new ArrayList<>();

        for (ITimeKeeperDetail newTimeKeeperDetail : timeKeeperDetails) {

            TimeKeeperBillingReport dbTimeKeeperBillingReport = new TimeKeeperBillingReport();

            //Billable Hours
            Double timeKeeperBillableHoursReportBillable = matterTimeTicketRepository.findTimeKeeperTimeTicketHoursBillable(
                    newTimeKeeperDetail.getTimeKeeperCode(), CLASSID,                //Class ID Hard code to 1 since it is L&E Report
                    timeKeeperBillingReportInput.getStartDate(),
                    timeKeeperBillingReportInput.getEndDate()
            );
            //Billed Hours
            Double timeKeeperBillableHoursReportBilledHours = matterTimeTicketRepository.findTimeKeeperTimeTicketHoursBilled(
                    newTimeKeeperDetail.getTimeKeeperCode(), CLASSID,                //Class ID Hard code to 1 since it is L&E Report
                    timeKeeperBillingReportInput.getStartDate(),
                    timeKeeperBillingReportInput.getEndDate()
            );
            //Billed Amount
            Double timeKeeperBillableHoursReportBilledAmount = matterTimeTicketRepository.findTimeKeeperTimeTicketBilledAmount(
                    newTimeKeeperDetail.getTimeKeeperCode(), CLASSID,                //Class ID Hard code to 1 since it is L&E Report
                    timeKeeperBillingReportInput.getStartDate(),
                    timeKeeperBillingReportInput.getEndDate()
            );
            //Non Billable Hours
            ITimeKeeperBillableHoursReport timeKeeperBillableHoursReportNonBillable = matterTimeTicketRepository.findTimeKeeperTimeTicketHoursNonBillable(
                    newTimeKeeperDetail.getTimeKeeperCode(), CLASSID,                //Class ID Hard code to 1 since it is L&E Report
                    timeKeeperBillingReportInput.getStartDate(),
                    timeKeeperBillingReportInput.getEndDate()
            );


            dbTimeKeeperBillingReport.setTimekeeperCode(newTimeKeeperDetail.getTimeKeeperCode());
            dbTimeKeeperBillingReport.setTimekeeperName(newTimeKeeperDetail.getTimeKeeperName());

            int workingDays = DateUtils.calculateWorkingDays(timeKeeperBillingReportInput.getStartDate(),
                    timeKeeperBillingReportInput.getEndDate());

            if (newTimeKeeperDetail.getUserType() == 1 || newTimeKeeperDetail.getUserType() == 2) {
                dbTimeKeeperBillingReport.setExpectedBillableHours((double) (workingDays * 7));
            }

            if (timeKeeperBillableHoursReportBillable != null) {

                dbTimeKeeperBillingReport.setBillableHours(timeKeeperBillableHoursReportBillable);
            }

            if (timeKeeperBillableHoursReportNonBillable != null) {

                dbTimeKeeperBillingReport.setNonBillableHours(timeKeeperBillableHoursReportNonBillable.getNonBillableHours());
            }

            log.info("billed hours: " + timeKeeperBillableHoursReportBilledHours);
            log.info("billed amount: " + timeKeeperBillableHoursReportBilledAmount);

            if (timeKeeperBillableHoursReportBilledHours != null) {
                dbTimeKeeperBillingReport.setBilledHours(timeKeeperBillableHoursReportBilledHours);
            }

            if (timeKeeperBillableHoursReportBilledAmount != null) {

                dbTimeKeeperBillingReport.setBilledFees(timeKeeperBillableHoursReportBilledAmount);

            }

            if (newTimeKeeperDetail.getDefaultRate() != null) {

                dbTimeKeeperBillingReport.setAverageBillingRate(newTimeKeeperDetail.getDefaultRate());
            }

            if (dbTimeKeeperBillingReport.getExpectedBillableHours() != null && dbTimeKeeperBillingReport.getBillableHours() != null) {

                dbTimeKeeperBillingReport.setRatio((dbTimeKeeperBillingReport.getBillableHours() /
                        dbTimeKeeperBillingReport.getExpectedBillableHours()) * 100);
            }

            if (dbTimeKeeperBillingReport.getExpectedBillableHours() != null && dbTimeKeeperBillingReport.getAverageBillingRate() != null) {

                dbTimeKeeperBillingReport.setExpectedBillingBasedOnExpectedBillableHours(
                        dbTimeKeeperBillingReport.getExpectedBillableHours() * dbTimeKeeperBillingReport.getAverageBillingRate());
            }

            if (dbTimeKeeperBillingReport.getBillableHours() != null && dbTimeKeeperBillingReport.getAverageBillingRate() != null) {

                dbTimeKeeperBillingReport.setExpectedBillingBasedOnLoggedBillableHours(
                        dbTimeKeeperBillingReport.getBillableHours() * dbTimeKeeperBillingReport.getAverageBillingRate());
            }

            if (dbTimeKeeperBillingReport.getBilledFees() != null && dbTimeKeeperBillingReport.getBilledHours() != null) {

                dbTimeKeeperBillingReport.setActualRates(dbTimeKeeperBillingReport.getBilledFees() /
                        dbTimeKeeperBillingReport.getBilledHours());
            }

            if (dbTimeKeeperBillingReport.getExpectedBillingBasedOnExpectedBillableHours() != null &&
                    dbTimeKeeperBillingReport.getBilledFees() != null) {

                dbTimeKeeperBillingReport.setExpectedFeeAndActualFeeRatio(
                        (dbTimeKeeperBillingReport.getBilledFees() /
                                dbTimeKeeperBillingReport.getExpectedBillingBasedOnExpectedBillableHours()) * 100);
            }

            timeKeeperBillingReportList.add(dbTimeKeeperBillingReport);
        }

        return timeKeeperBillingReportList;
    }
    
    //--------------------------------------TIME-TICKET-CR----------------------------------------------------------------------
    /*
     * Time ticket Notifications
     */
//    @Scheduled(cron = "0 0 4 * * MON,FRI,SAT")
//    @Scheduled(cron = "0 0 4 * * *")
//    @Scheduled(fixedRate = 5 * 60 * 1000)
//    public void scheduleTimeTicketNotifications() {
//        try {
//            Date currentDate = new Date();
//            Date[] previousWeekMondayAndFriday = DateUtils.getPreviousWeekMondayAndFriday();
//            Date[] firstDayOfWeek = DateUtils.addTimeToDatesForSearch(currentDate, currentDate);
//
//            log.info("previousWeekMondayAndFriday-------- : " + previousWeekMondayAndFriday[0] + "," + previousWeekMondayAndFriday[1]);
//            ;
//            log.info("firstDayOfWeek : " + firstDayOfWeek[0] + "," + firstDayOfWeek[1]);
//
//            List<ITimeTicketNotification> timeTickets =
//                    matterTimeTicketRepository.getTimeTicketDetails(previousWeekMondayAndFriday[0], previousWeekMondayAndFriday[1]);
//
//            List<ITimeTicketNotification> currentWeekTimeTickets =
//                    matterTimeTicketRepository.getTimeTicketDetailsByFirstDayOfWeek(previousWeekMondayAndFriday[0], previousWeekMondayAndFriday[1]);
////            List<ITimeTicketNotification> currentWeekTimeTickets =
////            		matterTimeTicketRepository.getTimeTicketDetailsByFirstDayOfWeek(previousWeekMondayAndFriday[0],
////            				previousWeekMondayAndFriday[1], firstDayOfWeek[0], firstDayOfWeek[1]);
//
//            List<TimeTicketNotification> timeTicketNotificationToBeSavedList = new ArrayList<>();
//
//            AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
//            timeTickets.stream().forEach(t -> {
//                log.info("prev timeTickets----> : " + t);
//                TimeTicketNotification ttNotification = new TimeTicketNotification();
//                TimekeeperCode timekeeperCode = setupService.getTimekeeperCode(t.getTimeKeeperCode(), authTokenForSetupService.getAccess_token());
//                if (timekeeperCode != null) {
//                    ttNotification.setClassId(timekeeperCode.getClassId());
//                }
//
//                ttNotification.setTimeKeeperCode(t.getTimeKeeperCode());
//                ttNotification.setTimeKeeperName("");
//                ttNotification.setFromDate(previousWeekMondayAndFriday[0]);
//                ttNotification.setToDate(previousWeekMondayAndFriday[1]);
//                ttNotification.setHoursPreviousWeek(t.getTimeTicketHours());
//                ttNotification.setAmountPreviousWeek(t.getTimeTicketAmount());
//                ttNotification.setWeekOfYear(getWeekOfTheYear());
//                ttNotification.setAmountCurrentWeek(0D);
//                ttNotification.setHoursCurrentWeek(0D);
//                ttNotification.setTimeKeeperAmount(t.getTimeTicketAmount());
//                ttNotification.setCreatedOn(currentDate);
//                LocalDate date = LocalDate.now();
//                ttNotification.setYear(Long.valueOf(date.getYear()));
//
//                /*
//                 * 0 - Compliance (HRS_SAME_WEEK = or greater than TGT_HRS
//                 * 1 - Non Compliance (HRS_SAME_WEEK less than TGT_HRS)
//                 */
//                if (t.getTimeTicketHours() < ttNotification.getTargetHours()) {
//                    ttNotification.setComplianceStatus(1L);
//                } else {
//                    ttNotification.setComplianceStatus(0L);
//                }
//                timeTicketNotificationToBeSavedList.add(ttNotification);
//            });
//            log.info("timeTicketNotificationToBeSavedList ----1---------> : " + timeTicketNotificationToBeSavedList);
//
//            currentWeekTimeTickets.stream().forEach(t -> {
//                log.info("currentWeekTimeTickets timeTickets----> : " + t);
//                timeTicketNotificationToBeSavedList.stream().forEach(l -> {
//                    if (l.getTimeKeeperCode().equalsIgnoreCase(t.getTimeKeeperCode())) {
//                        log.info("both ----1---------> : " + l.getTimeKeeperCode() + "," + t.getTimeKeeperCode());
//                        l.setAmountCurrentWeek(t.getTimeTicketAmount());
//                        l.setHoursCurrentWeek(t.getTimeTicketHours());
//
//                        Double totalHours = l.getHoursPreviousWeek() + l.getHoursCurrentWeek();
//                        l.setTimeKeeperHours(totalHours);
//
//                        Double prevWeekAmt = (l.getAmountPreviousWeek() != null) ? l.getAmountPreviousWeek() : 0D;
//                        Double currWeekAmt = (t.getTimeTicketAmount() != null) ? t.getTimeTicketAmount() : 0D;
//                        Double totalAmount = prevWeekAmt + currWeekAmt;
//                        log.info("Amount ----1---------> : " + prevWeekAmt + "," + currWeekAmt + "---" + totalAmount);
//
//                        l.setFromDate(previousWeekMondayAndFriday[0]);
//                        l.setToDate(previousWeekMondayAndFriday[1]);
//                        l.setTimeKeeperAmount(totalAmount);
//                        l.setCreatedOn(new Date());
//                    }
//                });
//                TimekeeperCode timekeeperCode = setupService.getTimekeeperCode(t.getTimeKeeperCode(), authTokenForSetupService.getAccess_token());
//                log.info("currentWeekTimeTickets----#1------> : " + t.getTimeKeeperCode() + "," + t.getTimeTicketAmount() + "," + t.getTimeTicketHours());
//                TimeTicketNotification ttNotification = new TimeTicketNotification();
////                ttNotification.setClassId(t.getClassId());
//                ttNotification.setClassId(timekeeperCode.getClassId());
//                ttNotification.setTimeKeeperCode(t.getTimeKeeperCode());
//                ttNotification.setTimeKeeperName("");
//                ttNotification.setFromDate(previousWeekMondayAndFriday[0]);
//                ttNotification.setToDate(previousWeekMondayAndFriday[1]);
//                ttNotification.setHoursPreviousWeek(0D);
//                ttNotification.setAmountPreviousWeek(0D);
//                ttNotification.setWeekOfYear(getWeekOfTheYear());
//                ttNotification.setAmountCurrentWeek(t.getTimeTicketAmount());
//                ttNotification.setHoursCurrentWeek(t.getTimeTicketHours());
//                ttNotification.setTimeKeeperAmount(t.getTimeTicketAmount());
//                ttNotification.setTimeKeeperHours(t.getTimeTicketHours());
//
//                ttNotification.setCreatedOn(currentDate);
//                LocalDate date = LocalDate.now();
//                ttNotification.setYear(Long.valueOf(date.getYear()));
//                ttNotification.setComplianceStatus(1L);
//                timeTicketNotificationToBeSavedList.add(ttNotification);
//            });
//            log.info("timeTicketNotificationToBeSavedList------2-------> : " + timeTicketNotificationToBeSavedList);
//
//            // Saving record to DB
//            timeTicketNotificationRepository.deleteTTNotification(getWeekOfTheYear());
//            timeTicketNotificationRepository.saveAll(timeTicketNotificationToBeSavedList);
//            log.info("TimeTicketNotifications Records saved....");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void scheduleTimeTicketNotifications() {
        try {
            Date currentDate = new Date();
            Date[] previousWeekMondayAndFriday = DateUtils.getPreviousWeekMondayAndFriday();
            Date[] firstDayOfWeek = DateUtils.addTimeToDatesForSearch(currentDate, currentDate);

            log.info("previousWeekMondayAndFriday-------- : " + previousWeekMondayAndFriday[0] + "," + previousWeekMondayAndFriday[1]);
            log.info("firstDayOfWeek : " + firstDayOfWeek[0] + "," + firstDayOfWeek[1]);

            List<ITimeTicketNotification> timeTickets =
                    matterTimeTicketRepository.getTimeTicketDetails(previousWeekMondayAndFriday[0], previousWeekMondayAndFriday[1]);

            // Filtering timkeeper
            List<String> timekeeper = timeTickets.stream().map(ITimeTicketNotification::getTimeKeeperCode).collect(Collectors.toList());
            List<ITimeTicketNotification> currentWeekTimeTickets =
                    matterTimeTicketRepository.getTimeTicketDetailsByFirstDayOfWeek(previousWeekMondayAndFriday[0], previousWeekMondayAndFriday[1]);
            List<TimeTicketNotification> timeTicketNotificationToBeSavedList = new ArrayList<>();

            AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
            timeTickets.stream().forEach(t -> {
                log.info("prev timeTickets----> : " + t);
                TimeTicketNotification ttNotification = new TimeTicketNotification();
                TimekeeperCode timekeeperCode = setupService.getTimekeeperCode(t.getTimeKeeperCode(), authTokenForSetupService.getAccess_token());
                if (timekeeperCode != null) {
                    ttNotification.setClassId(timekeeperCode.getClassId());
                }

                ttNotification.setTimeKeeperCode(t.getTimeKeeperCode());
                ttNotification.setTimeKeeperName(timekeeperCode.getTimekeeperName());
                ttNotification.setFromDate(previousWeekMondayAndFriday[0]);
                ttNotification.setToDate(previousWeekMondayAndFriday[1]);
                ttNotification.setHoursPreviousWeek(trimDecimals(t.getTimeTicketHours()));
                ttNotification.setAmountPreviousWeek(t.getTimeTicketAmount());
                ttNotification.setWeekOfYear(getWeekOfTheYear());
                ttNotification.setAmountCurrentWeek(0D);
                ttNotification.setHoursCurrentWeek(0D);
                ttNotification.setTimeKeeperAmount(t.getTimeTicketAmount());
                ttNotification.setTimeKeeperHours(trimDecimals(t.getTimeTicketHours())); 
                
                ttNotification.setCreatedOn(currentDate);
                LocalDate date = LocalDate.now();
                ttNotification.setYear(Long.valueOf(date.getYear()));

                /*
                 * 0 - Compliance (HRS_SAME_WEEK = or greater than TGT_HRS
                 * 1 - Non Compliance (HRS_SAME_WEEK less than TGT_HRS)
                 */
                if (t.getTimeTicketHours() < ttNotification.getTargetHours()) {
                    ttNotification.setComplianceStatus(1L);
                } else {
                    ttNotification.setComplianceStatus(0L);
                }
                timeTicketNotificationToBeSavedList.add(ttNotification);
            });
            log.info("timeTicketNotificationToBeSavedList ----1---------> : " + timeTicketNotificationToBeSavedList);

            currentWeekTimeTickets.stream().forEach(t -> {
                log.info("currentWeekTimeTickets timeTickets----> : " + t);
                timeTicketNotificationToBeSavedList.stream().forEach(l -> {
                    String currentWeekTimeKeeper = t.getTimeKeeperCode();
                    String previousWeekTimeKeeper = l.getTimeKeeperCode();
                    if (previousWeekTimeKeeper.equalsIgnoreCase(currentWeekTimeKeeper)) {
                        log.info("both ----1---------> : " + l.getTimeKeeperCode() + "," + t.getTimeKeeperCode());
                        l.setAmountCurrentWeek(t.getTimeTicketAmount());
                        l.setHoursCurrentWeek(trimDecimals(t.getTimeTicketHours()));

                        Double totalHours = l.getHoursPreviousWeek() + l.getHoursCurrentWeek();
                        l.setTimeKeeperHours(trimDecimals(totalHours));

                        Double prevWeekAmt = (l.getAmountPreviousWeek() != null) ? l.getAmountPreviousWeek() : 0D;
                        Double currWeekAmt = (t.getTimeTicketAmount() != null) ? t.getTimeTicketAmount() : 0D;
                        Double totalAmount = prevWeekAmt + currWeekAmt;
                        log.info("Amount ----1---------> : " + prevWeekAmt + "," + currWeekAmt + "---" + totalAmount);

                        l.setFromDate(previousWeekMondayAndFriday[0]);
                        l.setToDate(previousWeekMondayAndFriday[1]);
                        l.setTimeKeeperAmount(totalAmount);
                        l.setCreatedOn(new Date());
                    }
                });
                String currentTimeKeeper = t.getTimeKeeperCode();
                boolean userExists = timekeeper.stream().anyMatch(user -> currentTimeKeeper.equals(user));
                if(!userExists) {
                    TimekeeperCode timekeeperCode = setupService.getTimekeeperCode(t.getTimeKeeperCode(), authTokenForSetupService.getAccess_token());
                    log.info("currentWeekTimeTickets----#1------> : " + t.getTimeKeeperCode() + "," + t.getTimeTicketAmount() + "," + t.getTimeTicketHours());
                    TimeTicketNotification ttNotification = new TimeTicketNotification();
                    ttNotification.setClassId(t.getClassId());
                    ttNotification.setClassId(timekeeperCode.getClassId());
                    ttNotification.setTimeKeeperCode(t.getTimeKeeperCode());
                    ttNotification.setTimeKeeperName(timekeeperCode.getTimekeeperName());
                    ttNotification.setFromDate(previousWeekMondayAndFriday[0]);
                    ttNotification.setToDate(previousWeekMondayAndFriday[1]);
                    ttNotification.setHoursPreviousWeek(0D);
                    ttNotification.setAmountPreviousWeek(0D);
                    ttNotification.setWeekOfYear(getWeekOfTheYear());
                    ttNotification.setAmountCurrentWeek(t.getTimeTicketAmount());
                    ttNotification.setHoursCurrentWeek(trimDecimals(t.getTimeTicketHours()));
                    ttNotification.setTimeKeeperAmount(t.getTimeTicketAmount());
                    ttNotification.setTimeKeeperHours(trimDecimals(t.getTimeTicketHours()));

                    ttNotification.setCreatedOn(currentDate);
                    LocalDate date = LocalDate.now();
                    ttNotification.setYear(Long.valueOf(date.getYear()));
                    ttNotification.setComplianceStatus(1L);
                    timeTicketNotificationToBeSavedList.add(ttNotification);
                }
            }
            );
            log.info("timeTicketNotificationToBeSavedList------2-------> : " + timeTicketNotificationToBeSavedList);

            // Saving record to DB
            timeTicketNotificationRepository.deleteTTNotification(getWeekOfTheYear());
            timeTicketNotificationRepository.saveAll(timeTicketNotificationToBeSavedList);
            log.info("TimeTicketNotifications Records saved....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param value
     * @return
     */
    private Double trimDecimals (Double value) {
		DecimalFormat df = new DecimalFormat("#.##");      
		value = Double.valueOf(df.format(value));
		return value;
    }

    /**
     * @return
     */
    private Long getWeekOfTheYear() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        log.info("weekNumber---##----> " + weekNumber);
        long wkNumber = (weekNumber - 1);
        return wkNumber;
    }


    /**
     * @param findTimeTicketNotification
     * @return
     * @throws ParseException
     */
    public List<TimeTicketNotificationReport> findTimeTicketNotification(FindTimeTicketNotification findTimeTicketNotification)
            throws ParseException {
        TimeTicketNotificationSpecification spec = new TimeTicketNotificationSpecification(findTimeTicketNotification);
        List<TimeTicketNotification> results = timeTicketNotificationRepository.findAll(spec);
        List<TimeTicketNotificationReport> reportList = new ArrayList<>();
        for(TimeTicketNotification timeTicketNotification : results) {
            TimeTicketNotificationReport timeTicketNotificationReport = new TimeTicketNotificationReport();
            BeanUtils.copyProperties(timeTicketNotification, timeTicketNotificationReport, CommonUtils.getNullPropertyNames(timeTicketNotification));
            timeTicketNotificationReport.setScreatedOn(DateUtils.dateToString(timeTicketNotification.getCreatedOn()));
            timeTicketNotificationReport.setSfromDate(DateUtils.dateToString(timeTicketNotification.getFromDate()));
            timeTicketNotificationReport.setStoDate(DateUtils.dateToString(timeTicketNotification.getToDate()));
            reportList.add(timeTicketNotificationReport);
        }
        log.info("results: " + reportList);
        return reportList;
    }


    /**
     *
     * @param searchMatterTimeTicket
     * @return
     * @throws ParseException
     */
    public List<IMatterTimeTicket> searchMatterTimeTicket(SearchMatterTimeTicket searchMatterTimeTicket) throws ParseException{
        try{
            if (searchMatterTimeTicket.getStartCreatedOn() != null && searchMatterTimeTicket.getEndCreatedOn() != null) {
                Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTimeTicket.getStartCreatedOn(), searchMatterTimeTicket.getEndCreatedOn());
                searchMatterTimeTicket.setStartCreatedOn(dates[0]);
                searchMatterTimeTicket.setEndCreatedOn(dates[1]);
            }

            if (searchMatterTimeTicket.getStartTimeTicketDate() != null && searchMatterTimeTicket.getEndTimeTicketDate() != null) {
                Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterTimeTicket.getStartTimeTicketDate(),
                        searchMatterTimeTicket.getEndTimeTicketDate());
                searchMatterTimeTicket.setStartTimeTicketDate(dates[0]);
                searchMatterTimeTicket.setEndTimeTicketDate(dates[1]);
            }

            if(searchMatterTimeTicket.getTimeTicketNumber() == null || searchMatterTimeTicket.getTimeTicketNumber().isEmpty()){
                searchMatterTimeTicket.setTimeTicketNumber(null);
            }
            if(searchMatterTimeTicket.getMatterNumber() == null || searchMatterTimeTicket.getMatterNumber().isEmpty()){
                searchMatterTimeTicket.setMatterNumber(null);
            }
            if(searchMatterTimeTicket.getBillType() == null || searchMatterTimeTicket.getBillType().isEmpty()){
                searchMatterTimeTicket.setBillType(null);
            }
            if(searchMatterTimeTicket.getStatusId() == null || searchMatterTimeTicket.getStatusId().isEmpty()){
                searchMatterTimeTicket.setStatusId(null);
            }
            List<IMatterTimeTicket> data = matterTimeTicketRepository.getClientDesc(searchMatterTimeTicket.getTimeTicketNumber(),
                    searchMatterTimeTicket.getBillType(),
                    searchMatterTimeTicket.getTimeKeeperCode(),
                    searchMatterTimeTicket.getStatusId(),
                    searchMatterTimeTicket.getMatterNumber());
			return data;
        } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

}
