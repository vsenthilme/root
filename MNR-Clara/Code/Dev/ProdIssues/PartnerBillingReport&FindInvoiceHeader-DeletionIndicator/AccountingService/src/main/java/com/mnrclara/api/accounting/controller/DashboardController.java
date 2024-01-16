package com.mnrclara.api.accounting.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.mnrclara.api.accounting.model.management.SearchMatterGeneral;
import com.mnrclara.api.accounting.model.reports.ARReport;
import com.mnrclara.api.accounting.model.reports.BilledHoursPaid;
import com.mnrclara.api.accounting.model.reports.BilledHoursPaidReport;
import com.mnrclara.api.accounting.model.reports.BilledPaidFees;
import com.mnrclara.api.accounting.model.reports.BilledUnBilledHours;
import com.mnrclara.api.accounting.model.reports.ClientCashReceipts;
import com.mnrclara.api.accounting.model.reports.ExpirationDate;
import com.mnrclara.api.accounting.model.reports.SearchAR;
import com.mnrclara.api.accounting.service.DashboardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Dashboard"}, value = "DashboardController Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Dashboard")})
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
    /*
     * Dashboard - Billed Income
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/billedIncome")
	public ResponseEntity<?> billedIncome (@RequestParam Long classId, @RequestParam String period) throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.generateBilledIncomeDashboard(classId, period);
    	return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Case Assignment
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/caseAssignment")
	public ResponseEntity<?> caseAssignment (@RequestParam Long classId, 
			@RequestParam(defaultValue = "ResponsibleTimekeepers") String timeKeepers) throws Exception {
		CaseAssignmentDashboard dashboard = dashboardService.getCaseAssignmentDashboard(classId, timeKeepers);
    	return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Billable Unbillable Time
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard - Billable UnBillable Time") // label for swagger
	@GetMapping("/billableUnbillableTime")
	public ResponseEntity<BilledIncomeDashboard> getBillableNonBillableTime (@RequestParam("classId") Long classId,
													 @RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period) throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.getBillableNonBillableTime(classId, period);
		return new ResponseEntity<BilledIncomeDashboard>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Client Referral
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard - Client Referral") // label for swagger
	@GetMapping("/clientReferral")
	public ResponseEntity<BilledIncomeDashboard> getClientReferral (@RequestParam("classId") Long classId,
			@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period) throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.getClientReferral(classId, period);
		return new ResponseEntity<BilledIncomeDashboard>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Practice Breakdown
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard - Practice Breakdown") // label for swagger
	@GetMapping("/practiceBreakDown")
	public ResponseEntity<BilledIncomeDashboard> getPracticeBreakDown (@RequestParam("classId") Long classId,
		@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period) throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.getPracticeBreakDown(classId, period);
		return new ResponseEntity<BilledIncomeDashboard>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Top Clients
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Top Clients") // label for swagger
	@GetMapping("/topClients")
	public ResponseEntity<BilledIncomeDashboard> getTopClients (@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period)
			throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.getTopClients(period);
		return new ResponseEntity<BilledIncomeDashboard>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Lead Conversion
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Top Clients") // label for swagger
	@GetMapping("/leadConversion")
	public ResponseEntity<BilledIncomeDashboard> getLeadConversion (@RequestParam("classId") Long classId,
			@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period)
			throws Exception {
		BilledIncomeDashboard dashboard = dashboardService.getLeadConversion(classId,period);
		return new ResponseEntity<BilledIncomeDashboard>(dashboard, HttpStatus.OK);
	}

	/*
	 * Dashboard - Matter Listing
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Matter Listing") // label for swagger
	@PostMapping("/matter-listing")
	public ResponseEntity<?> getMatterListing (@RequestBody SearchMatterGeneral searchMatterGeneral)
			throws Exception {
		List<MatterListingReportImpl> data = dashboardService.getMatterListingData(searchMatterGeneral);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	/*
	 * Dashboard - Matter Rates Listing
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Matter Rates Listing") // label for swagger
	@PostMapping("/matter-rates-listing")
	public ResponseEntity<List<MatterListingReportImpl>> getMatterRatesListing (@RequestBody SearchMatterListing searchMatterListing)
			throws Exception {
		List<MatterListingReportImpl> data = dashboardService.getMatterRatesListingData(searchMatterListing);
		return new ResponseEntity<List<MatterListingReportImpl>>(data, HttpStatus.OK);
	}

	/*
	 * Dashboard - Billed UnBilled Hours
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Billed UnBilled Hours") // label for swagger
	@PostMapping("/billed-unbilled-hours")
	public ResponseEntity<List<BilledUnBillerHoursReportImpl>> getBilledUnBilledHours (@RequestBody BilledUnBilledHours billedUnBilledHours)
			throws Exception {
		List<BilledUnBillerHoursReportImpl> data = dashboardService.getBilledUnBilledHours(billedUnBilledHours);
		return new ResponseEntity<List<BilledUnBillerHoursReportImpl>>(data, HttpStatus.OK);
	}

	/*
	 * Dashboard - Client Cash Receipts Report
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Client Cash Receipts Report") // label for swagger
	@PostMapping("/client-cash-receipt-report")
	public ResponseEntity<List<ClientCashReceiptsReportImpl>> getClientCashReceipts (@RequestBody ClientCashReceipts clientCashReceipts)
			throws Exception {
		List<ClientCashReceiptsReportImpl> data = dashboardService.getClientCashReceipts(clientCashReceipts);
		return new ResponseEntity<List<ClientCashReceiptsReportImpl>>(data, HttpStatus.OK);
	}

	/*
	 * Dashboard - Client Income Summary Report
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  Client Income Summary Report") // label for swagger
	@PostMapping("/client-income-summary-report")
	public ResponseEntity<List<ClientIncomeSummaryReportImpl>> getClientIncomeSummary (@RequestBody ClientCashReceipts requestData)
			throws Exception {
		List<ClientIncomeSummaryReportImpl> data = dashboardService.getClientIncomeSummary(requestData);
		return new ResponseEntity<List<ClientIncomeSummaryReportImpl>>(data, HttpStatus.OK);
	}

	/*
	 * Dashboard - Client Income Summary Report
	 */
	@ApiOperation(response = Optional.class, value = "Dashboard -  AR Report") // label for swagger
	@PostMapping("/ar-report")
	public ResponseEntity<List<ARReport>> getARReport (@RequestBody SearchAR requestData)
			throws Exception {
		List<ARReport> data = dashboardService.getArReport(requestData);
		return new ResponseEntity<List<ARReport>>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Write Off Report") // label for swagger
	@PostMapping("/write-off-report")
	public ResponseEntity<List<WriteOffReportImpl>> getWriteOffReport (@RequestBody ClientCashReceipts requestData)
			throws Exception {
		List<WriteOffReportImpl> data = dashboardService.getWriteOffReport(requestData);
		return new ResponseEntity<List<WriteOffReportImpl>>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Billed Hours Paid Report") // label for swagger
	@PostMapping("/billed-hours-paid")
	public ResponseEntity<?> getBilledHoursPaidReport (@RequestBody BilledHoursPaid requestData)
			throws Exception {
		List<BilledHoursPaidReport> data = dashboardService.getBilledHoursPaidReport(requestData);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Billed Paid Fees Report") // label for swagger
	@PostMapping("/billed-paid-fees")
	public ResponseEntity<List<BilledPaidFeesImpl>> getBilledPaidFeesReport (@RequestBody BilledPaidFees requestData)
			throws Exception {
		List<BilledPaidFeesImpl> data = dashboardService.getBilledPaidFeesReport(requestData);
		return new ResponseEntity<List<BilledPaidFeesImpl>>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Expiration Date Report") // label for swagger
	@PostMapping("/expiration-date")
	public ResponseEntity<List<ExpirationDateReportImpl>> getExpirationDateReport (@RequestBody ExpirationDate requestData)
			throws Exception {
		List<ExpirationDateReportImpl> data = dashboardService.getExpirationDateReport(requestData);
		return new ResponseEntity<List<ExpirationDateReportImpl>>(data, HttpStatus.OK);
	}
}