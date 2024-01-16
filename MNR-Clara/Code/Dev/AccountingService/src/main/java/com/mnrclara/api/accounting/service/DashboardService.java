package com.mnrclara.api.accounting.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.dto.BilledIncomeDashboard;
import com.mnrclara.api.accounting.model.dto.CaseAssignmentDashboard;
import com.mnrclara.api.accounting.model.impl.BilledPaidFeesImpl;
import com.mnrclara.api.accounting.model.impl.BilledUnBillerHoursReportImpl;
import com.mnrclara.api.accounting.model.impl.ClientCashReceiptsReportImpl;
import com.mnrclara.api.accounting.model.impl.ClientIncomeSummaryReportImpl;
import com.mnrclara.api.accounting.model.impl.ExpirationDateReportImpl;
import com.mnrclara.api.accounting.model.impl.MatterListingReportImpl;
import com.mnrclara.api.accounting.model.impl.WriteOffReportImpl;
import com.mnrclara.api.accounting.model.invoice.report.SearchMatterListing;
import com.mnrclara.api.accounting.model.management.ClientGeneral;
import com.mnrclara.api.accounting.model.management.SearchMatterGeneral;
import com.mnrclara.api.accounting.model.prebill.MatterGenAcc;
import com.mnrclara.api.accounting.model.reports.ARReport;
import com.mnrclara.api.accounting.model.reports.BilledHoursPaid;
import com.mnrclara.api.accounting.model.reports.BilledHoursPaidReport;
import com.mnrclara.api.accounting.model.reports.BilledPaidFees;
import com.mnrclara.api.accounting.model.reports.BilledUnBilledHours;
import com.mnrclara.api.accounting.model.reports.ClientCashReceipts;
import com.mnrclara.api.accounting.model.reports.ExpirationDate;
import com.mnrclara.api.accounting.model.reports.IARReport;
import com.mnrclara.api.accounting.model.reports.IBilledHourReportInvoiceHeader;
import com.mnrclara.api.accounting.model.reports.IBilledHourReportMatterTimeTicket;
import com.mnrclara.api.accounting.model.reports.MatterListingReport;
import com.mnrclara.api.accounting.model.reports.SearchAR;
import com.mnrclara.api.accounting.repository.ARReportRepository;
import com.mnrclara.api.accounting.repository.ClientGeneralRepository;
import com.mnrclara.api.accounting.repository.InvoiceHeaderRepository;
import com.mnrclara.api.accounting.repository.InvoiceLineRepository;
import com.mnrclara.api.accounting.repository.MatterGenAccRepository;
import com.mnrclara.api.accounting.repository.MatterTimeTicketRepository;
import com.mnrclara.api.accounting.repository.PaymentUpdateRepository;
import com.mnrclara.api.accounting.repository.PreBillDetailsRepository;
import com.mnrclara.api.accounting.repository.specification.ARReportSpecification;
import com.mnrclara.api.accounting.repository.specification.MatterGenAccSpecification;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

    @Autowired
    InvoiceHeaderRepository invoiceHeaderRepository;
    
    @Autowired
    InvoiceLineRepository invoiceLineRepository;

    @Autowired
    MatterTimeTicketRepository matterTimeTicketRepository;

    @Autowired
    ClientGeneralRepository clientGeneralRepository;

    @Autowired
    MatterGenAccRepository matterGenAccRepository;
    
    @Autowired
    PreBillDetailsRepository preBillDetailsRepository;
    
    @Autowired
    PaymentUpdateRepository paymentUpdateRepository;
    
    @Autowired
    ARReportRepository arReportRepository;
    
    /*
     * Dropdown options( current month, current quarter, previous month, previous quarter,Current Year,Previous year)
     */
    public enum Period {
        CURRENTMONTH,
        CURRENTQUARTER,
        PREVIOUSMONTH,
        PREVIOUSQUARTER,
        CURRENTYEAR,
        PREVIOUSYEAR
    }

    public enum Timekeepers {
        PARTNERS,
        ORIGINATINGTIMEKEEPERS,
        RESPONSIBLETIMEKEEPERS,
        LEGALASSISTANTS
    }

    /**
     * @param classId
     * @param period
     * @return
     * @throws ParseException
     */
    public BilledIncomeDashboard generateBilledIncomeDashboard(Long classId, String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setCurrentMonth(currentMonth);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setCurrentQuarter(currentQuarter);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setCurrentYear(currentYear);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setPreviousMonth(previousMonth);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setPreviousQuarter(previousQuarter);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = invoiceHeaderRepository.getBilledIncome(classId, dates[0], dates[1]);
            dashboard.setPreviousYear(previousYear);
        }
        return dashboard;
    }

    /**
     * getCaseAssignmentDashboard
     *
     * @param classId
     * @param timeKeeper
     * @return
     */
    public CaseAssignmentDashboard getCaseAssignmentDashboard(Long classId, String timeKeeper) {
        CaseAssignmentDashboard caseAssignmentDashboard = new CaseAssignmentDashboard();

        // PARTNERS
        if (timeKeeper.equalsIgnoreCase(Timekeepers.PARTNERS.name())) {
            List<String[]> partners = invoiceHeaderRepository.getCaseAssignment(classId, timeKeeper);
            caseAssignmentDashboard.setPartners(partners);
        }

        // Originating Time keeper
        if (timeKeeper.equalsIgnoreCase(Timekeepers.ORIGINATINGTIMEKEEPERS.name())) {
            List<String[]> originatingTK = invoiceHeaderRepository.getCaseAssignment(classId, timeKeeper);
            caseAssignmentDashboard.setOriginatingTimekeepers(originatingTK);
        }

        // LEGALASSISTANTS
        if (timeKeeper.equalsIgnoreCase(Timekeepers.LEGALASSISTANTS.name())) {
            List<String[]> legalAssistants = invoiceHeaderRepository.getCaseAssignment(classId, timeKeeper);
            caseAssignmentDashboard.setLegalAssistants(legalAssistants);
        }

        // RESPONSIBLETIMEKEEPERS
        if (timeKeeper.equalsIgnoreCase(Timekeepers.RESPONSIBLETIMEKEEPERS.name())) {
            List<String[]> responsibleTK = invoiceHeaderRepository.getCaseAssignment(classId, timeKeeper);
            caseAssignmentDashboard.setResponsibleTimekeepers(responsibleTK);
        }

        return caseAssignmentDashboard;
    }

    /**
     * @param classId
     * @param period
     * @return
     * @throws Exception
     */
    public BilledIncomeDashboard getBillableNonBillableTime(Long classId, String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setCurrentMonth(currentMonth);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setCurrentQuarter(currentQuarter);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setCurrentYear(currentYear);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setPreviousMonth(previousMonth);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setPreviousQuarter(previousQuarter);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = matterTimeTicketRepository.getBillableNonBillableTime(classId, dates[0], dates[1]);
            dashboard.setPreviousYear(previousYear);
        }
        return dashboard;
    }

    /**
     * @param classId
     * @param period
     * @return
     * @throws Exception
     */
    public BilledIncomeDashboard getClientReferral(Long classId, String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setCurrentMonth(currentMonth);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setCurrentQuarter(currentQuarter);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setCurrentYear(currentYear);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setPreviousMonth(previousMonth);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setPreviousQuarter(previousQuarter);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = clientGeneralRepository.getClientReferral(classId, dates[0], dates[1]);
            dashboard.setPreviousYear(previousYear);
        }
        return dashboard;
    }

    /**
     * @param classId
     * @param period
     * @return
     * @throws Exception
     */
    public BilledIncomeDashboard getPracticeBreakDown(Long classId, String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setCurrentMonth(currentMonth);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setCurrentQuarter(currentQuarter);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setCurrentYear(currentYear);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setPreviousMonth(previousMonth);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setPreviousQuarter(previousQuarter);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = invoiceHeaderRepository.getPracticeBreakDown(classId, dates[0], dates[1]);
            dashboard.setPreviousYear(previousYear);
        }
        return dashboard;
    }

    /**
     * @param period
     * @return
     * @throws Exception
     */
    public BilledIncomeDashboard getTopClients(String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setCurrentMonth(currentMonth);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setCurrentQuarter(currentQuarter);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setCurrentYear(currentYear);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setPreviousMonth(previousMonth);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setPreviousQuarter(previousQuarter);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = matterTimeTicketRepository.getTopClients(dates[0], dates[1]);
            dashboard.setPreviousYear(previousYear);
        }
        return dashboard;
    }

    /**
     * 
     * @param classId
     * @param period
     * @return
     * @throws Exception
     */
    public BilledIncomeDashboard getLeadConversion(Long classId, String period) throws Exception {
        BilledIncomeDashboard dashboard = new BilledIncomeDashboard();

        List<String[]> calculatedList = new ArrayList<>();

        // Current Month
        if (period.equalsIgnoreCase(Period.CURRENTMONTH.name())) {
            Date[] dates = DateUtils.getCurrentMonth();
            List<String[]> currentMonth = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!currentMonth.isEmpty()) {
                calculatedList = getClientData(currentMonth, classId, dates[0], dates[1]);
            }
            dashboard.setCurrentMonth(calculatedList);
        }

        // Current Quarter
        if (period.equalsIgnoreCase(Period.CURRENTQUARTER.name())) {
            Date[] dates = DateUtils.getCurrentQuarter();
            List<String[]> currentQuarter = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!currentQuarter.isEmpty()) {
                calculatedList = getClientData(currentQuarter, classId, dates[0], dates[1]);
            }
            dashboard.setCurrentQuarter(calculatedList);
        }

        // Current Year
        if (period.equalsIgnoreCase(Period.CURRENTYEAR.name())) {
            Date[] dates = DateUtils.getCurrentYear();
            List<String[]> currentYear = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!currentYear.isEmpty()) {
                calculatedList = getClientData(currentYear, classId, dates[0], dates[1]);
            }
            dashboard.setCurrentYear(calculatedList);
        }

        // Previous Month
        if (period.equalsIgnoreCase(Period.PREVIOUSMONTH.name())) {
            Date[] dates = DateUtils.getPreviousMonth();
            List<String[]> previousMonth = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!previousMonth.isEmpty()) {
                calculatedList = getClientData(previousMonth, classId, dates[0], dates[1]);
            }
            dashboard.setPreviousMonth(calculatedList);
        }

        // Previous Quarter
        if (period.equalsIgnoreCase(Period.PREVIOUSQUARTER.name())) {
            Date[] dates = DateUtils.getPreviousQuarter();
            List<String[]> previousQuarter = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!previousQuarter.isEmpty()) {
                calculatedList = getClientData(previousQuarter, classId, dates[0], dates[1]);
            }
            dashboard.setPreviousQuarter(calculatedList);
        }

        // Previous Year
        if (period.equalsIgnoreCase(Period.PREVIOUSYEAR.name())) {
            Date[] dates = DateUtils.getPreviousYear();
            List<String[]> previousYear = invoiceHeaderRepository.getLeadConversion(classId, dates[0], dates[1]);
            if (!previousYear.isEmpty()) {
                calculatedList = getClientData(previousYear, classId, dates[0], dates[1]);
            }
            dashboard.setPreviousYear(calculatedList);
        }
        return dashboard;
    }

    /**
     * 
     * @param inquiryData
     * @param classId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<String[]> getClientData(List<String[]> inquiryData, Long classId, Date fromDate, Date toDate) {
        List<String[]> calculatedList = new ArrayList<>();
        List<String> assignedUser = new ArrayList<>();
        for(String[] newAssignedUser: inquiryData){
            String dbAssignedUser = newAssignedUser[0];
            assignedUser.add(dbAssignedUser);
        }
        List<String[]> assignedUserId = clientGeneralRepository.getAssignedUserId(assignedUser,classId,fromDate, toDate);
        for (String[] inquiry : inquiryData) {
            String[] columnArray = new String[4];
            // initial array values
            columnArray[0] = inquiry[0]; //assignedUserId
            columnArray[1] = inquiry[1]; //assignedUserIdCount
            columnArray[2] = "0"; // clientCountForPassedAssignedUserID- InquiryNumbers
            columnArray[3] = "0"; // calculation
            BigDecimal clientCountForInquiryNumber = clientGeneralRepository.getClientCountForInquiryNo(inquiry[0], classId, fromDate, toDate);
            if (clientCountForInquiryNumber != null && clientCountForInquiryNumber != BigDecimal.ZERO) {
                columnArray[2] = String.valueOf(clientCountForInquiryNumber);
                BigDecimal value = (clientCountForInquiryNumber.divide(BigDecimal.valueOf(Long.valueOf(inquiry[1])), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100L));
                columnArray[3] = String.valueOf(value.setScale(2, RoundingMode.HALF_UP));
            }
            calculatedList.add(columnArray);
        }
        //Assigned User doesn't have selected date range inquiry but created selected date range client
        for (String[] inquiry : assignedUserId) {
            String[] columnArray = new String[4];
            // initial array values
            columnArray[0] = inquiry[0]; //assignedUserId
            columnArray[1] = "0"; //currentMonthAssignedUserIdCount
            columnArray[2] = "0"; // clientCountForPassedAssignedUserID- InquiryNumbers
            columnArray[3] = "0"; // calculation
            BigDecimal clientCountForInquiryNumber = clientGeneralRepository.getClientCountForInquiryNo(inquiry[0], classId, fromDate, toDate);
            if (clientCountForInquiryNumber != null && clientCountForInquiryNumber != BigDecimal.ZERO) {
                columnArray[2] = String.valueOf(clientCountForInquiryNumber);
                BigDecimal value = (clientCountForInquiryNumber.divide(BigDecimal.valueOf(Long.valueOf(inquiry[1])), 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100L));
                columnArray[3] = String.valueOf(value.setScale(2, RoundingMode.HALF_UP));
            }
            calculatedList.add(columnArray);
        }

        return calculatedList;
    }

    /**
     * 
     * @param searchMatterGeneral
     * @return
     */
    public List<MatterListingReportImpl> getMatterListingData(SearchMatterGeneral searchMatterGeneral) {
        try {
			if (searchMatterGeneral.getClassId() == null || searchMatterGeneral.getClassId().isEmpty()) {
			    throw new BadRequestException("ClassId is a required field");
			}
			if (searchMatterGeneral.getStatusId() == null || searchMatterGeneral.getStatusId().isEmpty()) {
			    throw new BadRequestException("StatusId is a required field");
			}
			if (searchMatterGeneral.getBillingFrequency() == null || searchMatterGeneral.getBillingFrequency().isEmpty()) {
			    searchMatterGeneral.setBillingFrequency(null);
			}
			if (searchMatterGeneral.getCaseCategory() == null || searchMatterGeneral.getCaseCategory().isEmpty()) {
			    searchMatterGeneral.setCaseCategory(null);
			}
			if (searchMatterGeneral.getCaseSubCategory() == null || searchMatterGeneral.getCaseSubCategory().isEmpty()) {
			    searchMatterGeneral.setCaseSubCategory(null);
			}
			if (searchMatterGeneral.getPartner() == null || searchMatterGeneral.getPartner().isEmpty()) {
			    searchMatterGeneral.setPartner(null);
			}
			if (searchMatterGeneral.getOriginatingTimeKeeper() == null || searchMatterGeneral.getOriginatingTimeKeeper().isEmpty()) {
			    searchMatterGeneral.setOriginatingTimeKeeper(null);
			}
			if (searchMatterGeneral.getAssignedTimeKeeper() == null || searchMatterGeneral.getAssignedTimeKeeper().isEmpty()) {
			    searchMatterGeneral.setAssignedTimeKeeper(null);
			}
			if (searchMatterGeneral.getResponsibleTimeKeeper() == null || searchMatterGeneral.getResponsibleTimeKeeper().isEmpty()) {
			    searchMatterGeneral.setResponsibleTimeKeeper(null);
			}
			if (searchMatterGeneral.getParalegal() == null || searchMatterGeneral.getParalegal().isEmpty()) {
			    searchMatterGeneral.setParalegal(null);
			}
			if (searchMatterGeneral.getPetitionerName() == null || searchMatterGeneral.getPetitionerName().isEmpty()) {
			    searchMatterGeneral.setPetitionerName(null);
			}
			if (searchMatterGeneral.getCorporateName() == null || searchMatterGeneral.getCorporateName().isEmpty()) {
			    searchMatterGeneral.setCorporateName(null);
			}
			if (searchMatterGeneral.getLegalAssistant() == null || searchMatterGeneral.getLegalAssistant().isEmpty()) {
			    searchMatterGeneral.setLegalAssistant(null);
			}
			List<MatterListingReportImpl> data = matterGenAccRepository.getAllMatterListing(searchMatterGeneral.getClassId(),
			        searchMatterGeneral.getStatusId(),
			        searchMatterGeneral.getOriginatingTimeKeeper(),
			        searchMatterGeneral.getResponsibleTimeKeeper(),
			        searchMatterGeneral.getAssignedTimeKeeper(),
			        searchMatterGeneral.getLegalAssistant(),
			        searchMatterGeneral.getParalegal(),
			        searchMatterGeneral.getPartner(),
			        searchMatterGeneral.getCaseCategory(),
			        searchMatterGeneral.getCaseSubCategory(),
			        searchMatterGeneral.getBillingFrequency(),
			        searchMatterGeneral.getPetitionerName(),
			        searchMatterGeneral.getCorporateName());
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 
     * @param searchMatterListing
     * @return
     */
    public List<MatterListingReportImpl> getMatterRatesListingData(SearchMatterListing searchMatterListing) {
        try {
        	log.info("searchMatterListing : " + searchMatterListing);
			if (searchMatterListing.getClassId() == null) {
			    throw new BadRequestException("ClassId is a required field");
			}
			
			if (searchMatterListing.getStatusId() == null) {
			    throw new BadRequestException("StatusId is a required field");
			}
			
//			if (searchMatterListing.getClientId() == null && searchMatterListing.getClientId().isEmpty()) {
//			    searchMatterListing.setClientId(null);
//			}
//			if (searchMatterListing.getBillingFrequency() == null && searchMatterListing.getBillingFrequency().isEmpty()) {
//			    searchMatterListing.setBillingFrequency(null);
//			}
//			if (searchMatterListing.getCaseCategory() == null && searchMatterListing.getCaseCategory().isEmpty()) {
//			    searchMatterListing.setCaseCategory(null);
//			}
//			if (searchMatterListing.getCaseSubCategory() == null && searchMatterListing.getCaseSubCategory().isEmpty()) {
//			    searchMatterListing.setCaseSubCategory(null);
//			}
//			if (searchMatterListing.getTimeKeeper() == null && searchMatterListing.getTimeKeeper().isEmpty()) {
//			    searchMatterListing.setTimeKeeper(null);
//			}
//			if (searchMatterListing.getTimeKeeperStatus() == null && searchMatterListing.getTimeKeeperStatus().isEmpty()) {
//			    searchMatterListing.setTimeKeeperStatus(null);
//			}
			List<MatterListingReportImpl> data = matterGenAccRepository.getAllMatterRatesListing(searchMatterListing.getClassId(),
			        searchMatterListing.getStatusId(), searchMatterListing.getClientId(), searchMatterListing.getCaseCategory(), searchMatterListing.getCaseSubCategory(),
			        searchMatterListing.getTimeKeeper(), searchMatterListing.getTimeKeeperStatus(), searchMatterListing.getBillingFrequency());
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
   /**
    * 
    * @param matterListingReportImplList
    * @return
    */
    public List<MatterListingReport> convertToMatterListingReport (List<MatterListingReportImpl> matterListingReportImplList) {
        try {
			List<MatterListingReport> matterListingReportList = new ArrayList<>();
			for (MatterListingReportImpl implReport : matterListingReportImplList) {
				MatterListingReport report = new MatterListingReport();
				report.setMatterNumber(implReport.getMatterNumber());
				report.setMatterText(implReport.getMatterText());
				report.setClientId(implReport.getClientId());
				report.setFirstLastName(implReport.getFirstLastName());
				report.setCaseOpenedDate(implReport.getCaseOpenedDate());
				report.setCaseClosedDate(implReport.getCaseClosedDate());
				report.setBillModeText(implReport.getBillModeText());
				report.setBillFrequencyText(implReport.getBillFrequencyText());
				report.setCaseCategory(implReport.getCaseCategory());
				report.setCaseSubCategory(implReport.getCaseSubCategory());
				report.setResponsibleTk(implReport.getResponsibleTk());
				report.setOriginatingTk(implReport.getOriginatingTk());
				report.setTimeKeeperCode(implReport.getTimeKeeperCode());
				report.setAssignedRate(implReport.getAssignedRate());
				report.setPartner(implReport.getPartner());
				report.setAssignedTk(implReport.getAssignedTk());
				report.setLegalAssist(implReport.getLegalAssist());
				report.setParaLegal(implReport.getParaLegal());
				
				String clientCorpName = clientGeneralRepository.getCorpClientIdFirstName(implReport.getClientId());
				report.setCorporateName(clientCorpName);
				
				// Petitioner Name
				ClientGeneral clientGeneral = clientGeneralRepository.findByClientId(implReport.getClientId());
				if (clientGeneral != null) {
					if (clientGeneral.getClientCategoryId() == 4L) {
						report.setPetitionerName(clientGeneral.getReferenceField2());
					} else if (clientGeneral.getClientCategoryId() == 3L) {
						clientCorpName = clientGeneralRepository.getCorpClientIdFirstName(implReport.getClientId());
						report.setPetitionerName(clientCorpName);
					}
				}
				matterListingReportList.add(report);
			}
			log.info("matterListingReportList : " + matterListingReportList);
			return matterListingReportList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 
     * @param billedUnBilledHours
     * @return
     * @throws ParseException
     */
    public List<BilledUnBillerHoursReportImpl> getBilledUnBilledHours(BilledUnBilledHours billedUnBilledHours) throws ParseException {
        if (billedUnBilledHours.getClassId() == null || billedUnBilledHours.getClassId().isEmpty()) {
            throw new BadRequestException("ClassId is a required field");
        }
        if (billedUnBilledHours.getFromDate() == null || billedUnBilledHours.getToDate() == null) {
            throw new BadRequestException("Date Range is a required field");
        }
        if (billedUnBilledHours.getFromDate() != null &&
                billedUnBilledHours.getToDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(billedUnBilledHours.getFromDate(),
                    billedUnBilledHours.getToDate());
            billedUnBilledHours.setFromDate(dates[0]);
            billedUnBilledHours.setToDate(dates[1]);
        }
        if (billedUnBilledHours.getCaseCategory() == null && billedUnBilledHours.getCaseCategory().isEmpty()) {
            billedUnBilledHours.setCaseCategory(null);
        }
        if (billedUnBilledHours.getCaseSubCategory() == null && billedUnBilledHours.getCaseSubCategory().isEmpty()) {
            billedUnBilledHours.setCaseSubCategory(null);
        }
        if (billedUnBilledHours.getTimeKeeper() == null && billedUnBilledHours.getTimeKeeper().isEmpty()) {
            billedUnBilledHours.setTimeKeeper(null);
        }
        List<BilledUnBillerHoursReportImpl> data = matterGenAccRepository.getBilledUnBilledHours(billedUnBilledHours.getClassId(),
                billedUnBilledHours.getFromDate(), billedUnBilledHours.getToDate(), billedUnBilledHours.getTimeKeeper(), billedUnBilledHours.getCaseCategory(),
                billedUnBilledHours.getCaseSubCategory());
        return data;
    }

    /**
     * 
     * @param clientCashReceipts
     * @return
     * @throws ParseException
     */
    public List<ClientCashReceiptsReportImpl> getClientCashReceipts(ClientCashReceipts clientCashReceipts) throws ParseException {
        if (clientCashReceipts.getFromDate() != null &&
                clientCashReceipts.getToDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(clientCashReceipts.getFromDate(),
                    clientCashReceipts.getToDate());
            clientCashReceipts.setFromDate(dates[0]);
            clientCashReceipts.setToDate(dates[1]);
        } else {
            clientCashReceipts.setFromDate(null);
            clientCashReceipts.setToDate(null);
        }
        if (clientCashReceipts.getClientId() == null && clientCashReceipts.getClientId().isEmpty()) {
            clientCashReceipts.setClientId(null);
        }
        if (clientCashReceipts.getMatterNumber() == null && clientCashReceipts.getMatterNumber().isEmpty()) {
            clientCashReceipts.setMatterNumber(null);
        }
        List<ClientCashReceiptsReportImpl> data = matterGenAccRepository.getClientCashReceipts(clientCashReceipts.getMatterNumber(),
                clientCashReceipts.getFromDate(), clientCashReceipts.getToDate(), clientCashReceipts.getClientId());
        return data;
    }

    /**
     * 
     * @param requestData
     * @return
     * @throws ParseException
     */
    public List<ClientIncomeSummaryReportImpl> getClientIncomeSummary(ClientCashReceipts requestData) throws ParseException {
        if (requestData.getFromDate() != null &&
                requestData.getToDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(requestData.getFromDate(),
                    requestData.getToDate());
            requestData.setFromDate(dates[0]);
            requestData.setToDate(dates[1]);
        } else {
            throw new BadRequestException("Date Range is a required field");
        }
        
        if (requestData.getClientId() == null || requestData.getClientId().isEmpty()) {
            requestData.setClientId(null);
        }
        if (requestData.getMatterNumber() == null || requestData.getMatterNumber().isEmpty()) {
            requestData.setMatterNumber(null);
        }
        List<ClientIncomeSummaryReportImpl> results = matterGenAccRepository.getClientIncomeSummary(
                requestData.getFromDate(), requestData.getToDate(), requestData.getMatterNumber());
        log.info("ClientIncomeSummary-------- : " + results.size());
        
        if (requestData.getClientId() != null) {
        	List<ClientIncomeSummaryReportImpl> resultsResponseList = new ArrayList<>();
	        results.stream().forEach(a -> {
	        	if (requestData.getClientId().contains(a.getClientId())) {
	        		resultsResponseList.add (a);
	        	}
	        });
	        return resultsResponseList;
        }
        
        return results;
    }
    
    /**
     * 
     * @param searchAr
     * @return
     * @throws ParseException
     */
    public List<ARReport> getArReport (SearchAR searchAr) throws Exception {
        try {
			if (searchAr.getClassId() == null || searchAr.getClassId().isEmpty()) {
			    throw new BadRequestException("ClassId is a required field");
			}
			
			if (searchAr.getFromDate() == null || searchAr.getToDate() == null) {
			    throw new BadRequestException("Date is a required field");
			}
			
			Date[] dates = DateUtils.addTimeToDatesForSQL(searchAr.getFromDate(), searchAr.getToDate());
			List<String> matterNumberTTList = null;
			
			// When Closed Matters Check box is ticked - consider all the matters with all STATUS_ID
			if (!searchAr.getIncludeClosedMatter()) {	// Include specific StatusID
				if (searchAr.getTimeKeeper() != null && !searchAr.getTimeKeeper().isEmpty()) {
			    	matterNumberTTList = matterTimeTicketRepository.getMatterNumbersByTKCode(searchAr.getTimeKeeper(), searchAr.getClassId());
			    } else {
			    	matterNumberTTList = matterTimeTicketRepository.getMatterNumbers(searchAr.getClassId());
			    }
			} else {
				matterNumberTTList = matterTimeTicketRepository.getMatterNumbers(searchAr.getClassId());
			}
			
			searchAr.setMatterNumber(matterNumberTTList);
			searchAr.setFromDate(dates[0]);
			searchAr.setToDate(dates[1]);
			ARReportSpecification spec = new ARReportSpecification (searchAr);
			List<ARReport> searchResults = arReportRepository.findAll(spec);
			log.info("ARReport------searchResults----> : " + searchResults);
			return searchResults;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @throws Exception
     */
    @Scheduled(fixedRate=60*60*1000)
    public void scheduleARReport () throws Exception {
    	log.info("ARReport started.......: " + new Date());
    	
    	String fromDate_s = "2000-01-01";
		Date fromDate_d = DateUtils.convertStringToDate(fromDate_s);
        Date[] dates = DateUtils.addTimeToDatesForSQL(fromDate_d, new Date());

        List<String>  matterNumberTTList = matterTimeTicketRepository.getMatterNumbers(Arrays.asList(1L,2L));
        SearchMatterGeneral searchMatterGeneral = new SearchMatterGeneral();
        searchMatterGeneral.setClassId(Arrays.asList(1L,2L));
        searchMatterGeneral.setMatterNumber(matterNumberTTList);
        MatterGenAccSpecification spec = new MatterGenAccSpecification(searchMatterGeneral);
		List<com.mnrclara.api.accounting.model.prebill.MatterGenAcc> searchResults = matterGenAccRepository.findAll(spec);
		
		List<String> matterNumbers = 
				searchResults.stream()
				.map(com.mnrclara.api.accounting.model.prebill.MatterGenAcc::getMatterNumber)
				.collect(Collectors.toList());
		
		java.sql.Date date1 = DateUtils.convertUtilToSQLDate (dates[0]);
        java.sql.Date date2 = DateUtils.convertUtilToSQLDate (dates[1]);
        
        List<IARReport> arReportList = invoiceHeaderRepository.getInvoiceNumbersForARRpoer(matterNumbers, date1, date2);
        
        Set<ARReport> reportARReportList = new HashSet<>();
        Set<String> sMatterNos = new HashSet<>();
        for (IARReport a : arReportList) {
        	if (sMatterNos.add(a.getMatterNumber())) {
        		ARReport arReport = new ARReport();
            	arReport.setArrReportID(System.currentTimeMillis());
            	
            	com.mnrclara.api.accounting.model.prebill.MatterGenAcc matterGenAcc = matterGenAccRepository.findByMatterNumber(a.getMatterNumber());
            	arReport.setClassId(a.getClassId());
            	arReport.setClientId(matterGenAcc.getClientId());
            	arReport.setMatterNumber(a.getMatterNumber());
            	arReport.setPostingDate(a.getPostingDate());
            	
            	ClientGeneral clientGeneral = clientGeneralRepository.findByClientId(matterGenAcc.getClientId());
            	if (clientGeneral != null) {
            		arReport.setClientName(clientGeneral.getFirstNameLastName());
            		arReport.setPhone(clientGeneral.getContactNumber());
            	}
            	
            	arReport.setMatterText(matterGenAcc.getMatterDescription());
            	if (matterGenAcc.getBillingFormatId() != null) {
            		arReport.setBillingFormat(matterGenAccRepository.getBillFormatText (Long.valueOf(matterGenAcc.getBillingFormatId())));
            	}
            	
            	try {
    				var date11 = DateUtils.addTimeToSingleFromDateForSearch(LocalDate.ofInstant(dates[0].toInstant(), 
    								ZoneId.systemDefault()));
    				var date12 = DateUtils.addTimeToSingleFromDateForSearch(LocalDate.ofInstant(dates[1].toInstant(), 
    						ZoneId.systemDefault()));
    				
    				Double totalSumOfPayment = paymentUpdateRepository.getPmtRec(a.getMatterNumber(), date11, date12);
    				totalSumOfPayment = (totalSumOfPayment != null ? totalSumOfPayment : 0D);

    				// INVOICE_AMT
    				Double totalAmountDue = invoiceHeaderRepository.getInvAmt(a.getMatterNumber(), totalSumOfPayment);
    				totalAmountDue = (totalAmountDue != null ? totalAmountDue : 0D);
    				arReport.setTotalDue(totalAmountDue);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            	
            	Date lastPaymentDate = paymentUpdateRepository.getLastPaymentDateByMatterNumber (a.getMatterNumber());
            	arReport.setLastPaymentDate(lastPaymentDate);
            	
            	Date lastBillDate = invoiceHeaderRepository.getLastBillDateByMatterNumber(a.getMatterNumber());
            	arReport.setLastBillDate(lastBillDate);
            	reportARReportList.add(arReport);
        	}
        }
		arReportRepository.deleteAll();
		arReportRepository.saveAll(reportARReportList);
		log.info("ARReport done.......: " + reportARReportList.size());
    }
    
    /**
     * 
     * @param requestData
     * @return
     * @throws ParseException
     */
    public List<WriteOffReportImpl> getWriteOffReport(ClientCashReceipts requestData) throws ParseException {
        if (requestData.getFromDate() != null &&
                requestData.getToDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(requestData.getFromDate(),
                    requestData.getToDate());
            requestData.setFromDate(dates[0]);
            requestData.setToDate(dates[1]);
        } else {
            throw new BadRequestException("Date range is a required field");
        }
        if (requestData.getClientId() == null && requestData.getClientId().isEmpty()) {
            requestData.setClientId(null);
        }
        if (requestData.getMatterNumber() == null && requestData.getMatterNumber().isEmpty()) {
            requestData.setMatterNumber(null);
        }
        if (requestData.getTimeKeeperCode() == null && requestData.getTimeKeeperCode().isEmpty()) {
            requestData.setTimeKeeperCode(null);
        }
        List<WriteOffReportImpl> data = matterGenAccRepository.getWriteOffReport(requestData.getMatterNumber(),
                requestData.getFromDate(), requestData.getToDate(), requestData.getClientId(), requestData.getTimeKeeperCode());
        List<WriteOffReportImpl> filteredData = data
                .stream()
                .filter(c -> c.getTotal() != 0D)
                .collect(Collectors.toList());
        return filteredData;
    }

    /**
     * 
     * @param requestData
     * @return
     * @throws ParseException
     */
    public List<BilledHoursPaidReport> getBilledHoursPaidReport(BilledHoursPaid requestData) throws ParseException {
    	 if (requestData.getFromPostingDate() != null && requestData.getToPostingDate() != null) {
             Date[] dates = DateUtils.addTimeToDatesForSearch(requestData.getFromPostingDate(), requestData.getToPostingDate());
             requestData.setFromPostingDate(dates[0]);
             requestData.setToPostingDate(dates[1]);
         } 
    	 
    	 List<IBilledHourReportInvoiceHeader> invoiceHeaderDetails = null;
    	 if (requestData.getMatterNumber() == null && requestData.getClientId() == null) {
    		 invoiceHeaderDetails = 
    				 invoiceHeaderRepository.getSumOfInvoiceAmtWOMatterNumberAndClientID(requestData.getFromPostingDate(), requestData.getToPostingDate());
    	 } else if (requestData.getMatterNumber() != null && requestData.getClientId() == null) {
    		 invoiceHeaderDetails = 
        			 invoiceHeaderRepository.getSumOfInvoiceAmtByMatterNumber(requestData.getFromPostingDate(), requestData.getToPostingDate(),
        					 requestData.getMatterNumber());
    	 } else if (requestData.getMatterNumber() == null && requestData.getClientId() != null) {
    		 invoiceHeaderDetails = 
        			 invoiceHeaderRepository.getSumOfInvoiceAmtByClientID(requestData.getFromPostingDate(), requestData.getToPostingDate(),
        					 requestData.getClientId());
    	 } else if (requestData.getMatterNumber() != null && requestData.getClientId() != null) {
    		 invoiceHeaderDetails = 
        			 invoiceHeaderRepository.getSumOfInvoiceAmtByMatterNumberAndClientID(requestData.getFromPostingDate(), requestData.getToPostingDate(),
        					 requestData.getMatterNumber(), requestData.getClientId());
    	 }
    	 
    	 List<BilledHoursPaidReport> results = new ArrayList<>();
    	 try {
			 invoiceHeaderDetails.stream().forEach(a -> {
				 BilledHoursPaidReport response = new BilledHoursPaidReport();
				 
				 response.setMatterNumber(a.getMatterNumber());
				 MatterGenAcc matterGenAcc = matterGenAccRepository.findByMatterNumber(a.getMatterNumber());
				 if (matterGenAcc != null) {
					 response.setMatterText(matterGenAcc.getMatterDescription());
				 }
				 
				 response.setInvoiceNumber(a.getInvoiceNumber());
				 response.setDateOfBill(a.getDateOfBill());
				 log.info("a.getMatterNumber() : " + a.getMatterNumber());	
				 log.info("getFromPostingDate() : " + requestData.getFromPostingDate());	
				 log.info("getToPostingDate() : " + requestData.getToPostingDate());	
				 
				 Double paymentAmount = invoiceHeaderRepository.getSumOfPaymentAmtByMatterNumber(requestData.getFromPostingDate(), 
						 requestData.getToPostingDate(), a.getMatterNumber());
				 paymentAmount = (paymentAmount != null) ? paymentAmount : 0D;
				 log.info("paymentAmount : " + paymentAmount);				
				 
				 if (paymentAmount != 0D) {
					 Double dividedAmount = (paymentAmount / a.getInvoiceAmount());
					 dividedAmount = (dividedAmount != null) ? dividedAmount : 0D;
					 
					 IBilledHourReportMatterTimeTicket mttAppDetails = null;
					 if (requestData.getTimeKeeperCode() != null) {
						 mttAppDetails = invoiceHeaderRepository.getMatterTimeTicketByMatterNumberWithTKCode(a.getPreBillNumber(), a.getMatterNumber(),
										 requestData.getTimeKeeperCode());
					 } else {
						 mttAppDetails = invoiceHeaderRepository.getMatterTimeTicketByMatterNumberWOTKCode(a.getPreBillNumber(), a.getMatterNumber());
					 }
					 
					 Double amountBilled = 0D;
					 Double hoursBilled = 0D;
					 if (mttAppDetails != null) {
						 response.setAttorney(mttAppDetails.getAttorney());
						 response.setHoursBilled(mttAppDetails.getHoursBilled());
						 response.setAmountBilled(mttAppDetails.getAmountBilled());
						 hoursBilled = (mttAppDetails.getHoursBilled() != null) ? mttAppDetails.getHoursBilled() : 0D;
						 amountBilled = (mttAppDetails.getAmountBilled() != null) ? mttAppDetails.getAmountBilled() : 0D;
					 }
					
					 Double approxAmountReceived = (dividedAmount * amountBilled);
					 response.setApproxAmountReceived(approxAmountReceived);
					 
					 Double calculatedBillHours = (amountBilled / hoursBilled);
					 Double approxBilledhours = (approxAmountReceived / calculatedBillHours);
					 response.setApproxHoursPaid(approxBilledhours);
				 }
				 
				 log.info("response.......: " + response);
				 results.add(response);
				});
		} catch (Exception e) {
			e.printStackTrace();
		}  	 
    	 
    	 return results;
    }

    /**
     * 
     * @param requestData
     * @return
     * @throws ParseException
     */
    public List<BilledPaidFeesImpl> getBilledPaidFeesReport(BilledPaidFees requestData) throws ParseException {

        if (requestData.getMonth() == 0 || requestData.getYear() == 0) {
            throw new BadRequestException("Posting Date range is a required field");
        }

        Calendar c = Calendar.getInstance();
        c.set(requestData.getYear(), requestData.getMonth() - 1, 1, 0, 0);

        Date[] dates = DateUtils.addTimeToDatesForSearch(c.getTime(),
                new Date());

        LocalDate today = LocalDate.now().withDayOfMonth(1).withMonth(1);
        var yearStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date[] yearStartDate = DateUtils.addTimeToDatesForSearch(yearStart,
                new Date());


        if (requestData.getCaseCategoryId() == null && requestData.getCaseCategoryId().isEmpty()) {
            requestData.setCaseCategoryId(null);
        }
        if (requestData.getTimeKeeperCode() == null && requestData.getTimeKeeperCode().isEmpty()) {
            requestData.setTimeKeeperCode(null);
        }
        List<BilledPaidFeesImpl> data = matterGenAccRepository.getBilledPaidFeesReport(
                dates[0], dates[1], requestData.getCaseCategoryId(), requestData.getTimeKeeperCode(),yearStartDate[0]);
        return data;
    }

    /**
     * 
     * @param requestData
     * @return
     * @throws ParseException
     */
    public List<ExpirationDateReportImpl> getExpirationDateReport(ExpirationDate requestData) throws ParseException {
        if (requestData.getFromExpirationDate() != null &&
                requestData.getToExpirationDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(requestData.getFromExpirationDate(),
                    requestData.getToExpirationDate());
            requestData.setFromExpirationDate(dates[0]);
            requestData.setToExpirationDate(dates[1]);
        } else {
            requestData.setFromExpirationDate(null);
            requestData.setToExpirationDate(null);
        }
        if (requestData.getFromEligibilityDate() != null &&
                requestData.getToEligibilityDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(requestData.getFromEligibilityDate(),
                    requestData.getToEligibilityDate());
            requestData.setFromEligibilityDate(dates[0]);
            requestData.setToEligibilityDate(dates[1]);
        } else {
            requestData.setFromEligibilityDate(null);
            requestData.setToEligibilityDate(null);
        }
        if (requestData.getCaseCategoryId() == null || requestData.getCaseCategoryId().isEmpty()) {
            requestData.setCaseCategoryId(null);
        }
        if (requestData.getCaseSubCategoryId() == null || requestData.getCaseSubCategoryId().isEmpty()) {
            requestData.setCaseSubCategoryId(null);
        }
        if (requestData.getTimeKeeperCode() == null || requestData.getTimeKeeperCode().isEmpty()) {
            requestData.setTimeKeeperCode(null);
        }
        if (requestData.getDocumentType() == null || requestData.getDocumentType().isEmpty()) {
            requestData.setDocumentType(null);
        }
        if (requestData.getReceiptNumber() == null || requestData.getReceiptNumber().isEmpty()) {
            requestData.setReceiptNumber(null);
        }
        List<ExpirationDateReportImpl> data = matterGenAccRepository.getExpirationDateReport(requestData.getCaseCategoryId(),
                requestData.getCaseSubCategoryId(), requestData.getReceiptNumber(), requestData.getTimeKeeperCode(), requestData.getDocumentType(),
                requestData.getFromExpirationDate(), requestData.getToExpirationDate(), requestData.getFromEligibilityDate(),
                requestData.getToEligibilityDate());
        return data;
    }
}
