package com.mnrclara.api.management.controller;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.mnrclara.api.management.model.mattergeneral.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.BillByGroup;
import com.mnrclara.api.management.model.dto.DashboardReport;
import com.mnrclara.api.management.model.dto.Dropdown;
import com.mnrclara.api.management.model.dto.ImmigrationMatter;
import com.mnrclara.api.management.model.dto.LNEMatter;
import com.mnrclara.api.management.model.dto.MatterDropdownList;
import com.mnrclara.api.management.model.dto.MatterTimeExpenseTicket;
import com.mnrclara.api.management.service.AccountingService;
import com.mnrclara.api.management.service.MatterGenAccService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MatterGeneralAccount" }, value = "MatterGeneralAccount Operations related to MatterGeneralAccount")
@SwaggerDefinition(
		tags = {
			@Tag(name = "MatterGeneralAccount", description = "Operations related to MatterGeneralAccount") 
		}
)
@RequestMapping("/mattergenacc")
@RestController
public class MatterGenAccController {

	@Autowired
	MatterGenAccService matterGenAccService;
	
	@Autowired
	AccountingService accountingService;

	@ApiOperation(response = MatterGenAcc.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterGenAcc> matterGenAccList = matterGenAccService.getMatterGenAccs();
		return new ResponseEntity<>(matterGenAccList, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "MATTER_NO") String sortBy,
									@RequestParam List<Long> classId) {
		Page<MatterGenAcc> list = matterGenAccService.getAllMatterGenerals(pageNo, pageSize, sortBy, classId);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
	
	@ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/dropdown")
	public ResponseEntity<?> getDropdownList() {
		Dropdown matterGenAcc = matterGenAccService.getDropdownList();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/dropdown/matter")
	public ResponseEntity<?> getMatterDropdownList() {
		MatterDropdownList matterGenAcc = matterGenAccService.getMatterDropdownList();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/dropdown/matter/open")
	public ResponseEntity<?> getOpenMatterDropdownList() {
		MatterDropdownList matterGenAcc = matterGenAccService.getOpenMatterDropdownList();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGeneralAccount") // label for swagger
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterGenAcc(@PathVariable String matterNumber) {
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(matterNumber);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGeneralAccount OutputForm") // label for swagger
	@GetMapping("/outputForm")
	public ResponseEntity<?> getMatterGenAcc(@RequestParam List<String> matterNumber) throws ParseException {
		List<MatterGenAcc> matterGenAcc = matterGenAccService.getMatterGenAcc(matterNumber);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGeneralAccount") // label for swagger
	@GetMapping("/{matterNumber}/docketwise/{matterId}")
	public ResponseEntity<?> getMatterGenAccFromDocketwise(@PathVariable String matterNumber, @PathVariable String matterId) 
			throws ParseException, java.text.ParseException {
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAccFromDocketwise(matterNumber, matterId);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGenAcc") // label for swagger
	@GetMapping("/top")
	public ResponseEntity<?> getTopMatterGenAccGeneral() {
		MatterGenAcc matterGenAcc = matterGenAccService.getLatestMatterGeneral();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGenAcc") // label for swagger
	@GetMapping("/receiptPaymentQuery")
	public ResponseEntity<?> getReceiptPaymentQueryNumber() {
		MatterGenAcc matterGenAcc = matterGenAccService.getReceiptPaymentQueryNumber();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGenAcc") // label for swagger
	@GetMapping("/limit")
	public ResponseEntity<?> getMatterGenAccGeneralByLimit() {
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGeneralByLimit();
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Search MatterGenAcc") // label for swagger
    @PostMapping("/findMatterGenAccs")
    public List<MatterGenAcc> findMatterGenAccs(@RequestBody SearchMatterGeneral searchMatterGeneral) throws ParseException {
		return matterGenAccService.findMatterGenAccs(searchMatterGeneral);
	}

	//--------------------------------------------Find Matter General------------------------------------------------------------------------
	@ApiOperation(response = Optional.class, value = "Find Matter General New") // label for swagger
	@PostMapping("/findMatterGeneralNew")
	public ResponseEntity<List<FindMatterGenImpl>> getMatterGeneral (@RequestBody FindMatterGeneral searchMatterGeneral)
			throws Exception {
		List<FindMatterGenImpl> data = matterGenAccService.findMatterGeneral(searchMatterGeneral);
		return new ResponseEntity<List<FindMatterGenImpl>>(data, HttpStatus.OK);
	}
	
	//-----------------------------Conflict-Check-----------------------------------------------------
	@ApiOperation(response = ClientGeneral.class, value = "Search MatterGenAcc") // label for swagger
	@GetMapping("/findRecords")
	public List<MatterGenAcc> findRecords(@RequestParam String fullTextSearch) throws ParseException {
		return matterGenAccService.findRecords(fullTextSearch);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Total") // label for swagger
	@GetMapping("/dashboard/total")
	public ResponseEntity<?> getDashboardTotal(@RequestParam String loginUserID) {
		DashboardReport dashboardReport = matterGenAccService.getDashboardTotal(loginUserID);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Open") // label for swagger
	@GetMapping("/dashboard/open")
	public ResponseEntity<?> getDashboardOpen(@RequestParam String loginUserID) {
		Long statusId = 26L;
		DashboardReport dashboardReport = matterGenAccService.getDashboardCountByStatus(statusId, loginUserID);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Filed") // label for swagger
	@GetMapping("/dashboard/filed")
	public ResponseEntity<?> getDashboardFiled(@RequestParam String loginUserID) {
		Long statusId = 27L;
		DashboardReport dashboardReport = matterGenAccService.getDashboardCountByStatus(statusId, loginUserID);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - RTE") // label for swagger
	@GetMapping("/dashboard/RTE")
	public ResponseEntity<?> getDashboardRTE(@RequestParam String loginUserID) {
		Long statusId = 28L;
		DashboardReport dashboardReport = matterGenAccService.getDashboardCountByStatus(statusId, loginUserID);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - CLOSED") // label for swagger
	@GetMapping("/dashboard/closed")
	public ResponseEntity<?> getDashboardClosed(@RequestParam String loginUserID) {
		Long statusId = 30L;
		DashboardReport dashboardReport = matterGenAccService.getDashboardCountByStatus(statusId, loginUserID);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Create Bulk Matter") // label for swagger
	@PostMapping("/batch")
	public ResponseEntity<?> postBulkMatters(@RequestBody AddMatterGenAcc[] newMatterGenAccs, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
		matterGenAccService.createBulkMatters(newMatterGenAccs, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = MatterGenAcc.class, value = "Update MatterGeneralAccount") // label for swagger
	@PatchMapping("/{matterNumber}")
	public ResponseEntity<?> patchMatterGenAcc(@PathVariable String matterNumber,
			@Valid @RequestBody MatterGenAcc updateMatterGenAcc, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException, java.text.ParseException {
		MatterGenAcc updatedMatterGenAcc = matterGenAccService.updateMatterGenAcc(matterNumber, updateMatterGenAcc,
				loginUserID);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Update MatterGeneralAccount") // label for swagger
	@GetMapping("/{matterNumber}/qbSync")
	public ResponseEntity<?> patchQbSync(@PathVariable String matterNumber, @RequestParam String loginUserID)
			throws Exception {
		MatterGenAcc updatedMatterGenAcc = matterGenAccService.updateMatterGenAccByQBSync(matterNumber, loginUserID);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Update MatterGeneralAccount") // label for swagger
	@PatchMapping("/{matterNumber}/accounting")
	public ResponseEntity<?> patchMatterAccounting(@PathVariable String matterNumber,
			@Valid @RequestBody UpdateMatterGenAcc updateMatterGenAcc, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterGenAcc updatedMatterGenAcc = matterGenAccService.updateMatterAccounting(matterNumber, updateMatterGenAcc,
				loginUserID);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = MatterGenAcc.class, value = "Delete MatterGeneralAccount") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterGenAcc(@PathVariable String matterNumber, @RequestParam String loginUserID) {
		matterGenAccService.deleteMatterGenAcc(matterNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//--------------------Push-To-Docketwise-------------------------------------------------------------------------
	@ApiOperation(response = MatterGenAcc.class, value = "Push2Docketwise MatterGeneral") // label for swagger
	@GetMapping("/{matterNumber}/push2Docketwise")
	public ResponseEntity<?> push2Docketwise(@PathVariable String matterNumber) {
		MatterGenAcc updatedMatterGenAcc = matterGenAccService.push2Docketwise(matterNumber);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}
	
	//--------------------AccountingService-PreBill-Functionality-----------------------------------------------------
	@ApiOperation(response = MatterGenAcc.class, value = "PrebillByGroup-Hourly") // label for swagger
	@PostMapping("/accounting/prebill/individual")
	public ResponseEntity<?> prebillByIndividual(@RequestBody BillByGroup billByGroup) throws ParseException {
		List<MatterTimeExpenseTicket> matterTimeExpenseTicket = 
				accountingService.getMatterTimeNExpenseTicketsByIndividual(billByGroup);
		return new ResponseEntity<>(matterTimeExpenseTicket, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "PrebillByGroup-Hourly") // label for swagger
	@PostMapping("/accounting/prebill/group")
	public ResponseEntity<?> prebillByGroup(@RequestBody BillByGroup billByGroup) {
		List<MatterTimeExpenseTicket> matterTimeExpenseTicket = 
				accountingService.getMatterTimeNExpenseTicketsByGroup(billByGroup);
		return new ResponseEntity<>(matterTimeExpenseTicket, HttpStatus.OK);
	}
	
	//--------------------------Reports-----------------------------------------------------------
	@ApiOperation(response = MatterImmigrationReport[].class, value = "Matter Immigration Report") // label for swagger
	@PostMapping("/immigrationReport")
	public ResponseEntity<?> getMatterImmigrationReport (@RequestBody ImmigrationMatter immigrationMatter) 
			throws ParseException {
		List<ImmigrationReportImpl> matterImmigrationReport =
				matterGenAccService.getMatterImmigrationReport(immigrationMatter);
		return new ResponseEntity<>(matterImmigrationReport, HttpStatus.OK);
	}

	@ApiOperation(response = AttorneyProductivityReport[].class, value = "Attorney Production Report") // label for swagger
	@PostMapping("/attorneyProductivityReport")
	public ResponseEntity<?> getAttorneyProductionreport (@RequestBody AttorneyProductivityInput attorneyProductivityInput)
			throws ParseException {
		List<AttorneyProductivityReport> attorneyProductivityReports =
				matterGenAccService.getAttorneyProductivityReport(attorneyProductivityInput);
		return new ResponseEntity<>(attorneyProductivityReports, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterLNEReport[].class, value = "Matter LNE Report") // label for swagger
	@PostMapping("/lneReport")
	public ResponseEntity<?> getMatterLNEReport (@RequestBody LNEMatter lneMatter) 
			throws ParseException {
		List<MatterLNEReport> matterLNEReport = matterGenAccService.getMatterLNEReport(lneMatter);
		return new ResponseEntity<>(matterLNEReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = WIPAgedPBReport[].class, value = "WIPAgedPBReport") // label for swagger
	@PostMapping("/wipAgedPBReport")
	public ResponseEntity<?> getWIPAgedPBReport (@RequestBody WIPAgedPBReportInput wipAgedPBReportInput) 
			throws Exception {
		List<WIPAgedPBReport> wipAgedPBReport = matterGenAccService.getWIPAgedPBReport(wipAgedPBReportInput);
		return new ResponseEntity<>(wipAgedPBReport, HttpStatus.OK);
	}
}