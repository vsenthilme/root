package com.mnrclara.api.accounting.controller;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.accounting.model.impl.PartnerBillingReportImpl;
import com.mnrclara.api.accounting.model.invoice.report.*;
import com.mnrclara.api.accounting.model.reports.MatterPLReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.accounting.model.invoice.AddInvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.InvoicePreBillDetails;
import com.mnrclara.api.accounting.model.invoice.InvoiceRet;
import com.mnrclara.api.accounting.model.invoice.PaymentUpdate;
import com.mnrclara.api.accounting.model.invoice.ReceivePaymentResponse;
import com.mnrclara.api.accounting.model.invoice.SearchInvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.SearchPaymentUpdate;
import com.mnrclara.api.accounting.model.invoice.TransferBilling;
import com.mnrclara.api.accounting.model.invoice.UpdateInvoiceHeader;
import com.mnrclara.api.accounting.model.prebill.SearchPreBillDetails;
import com.mnrclara.api.accounting.service.InvoiceHeaderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"InvoiceHeader"}, value = "InvoiceHeader  Operations related to InvoiceHeaderController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "InvoiceHeader ",description = "Operations related to InvoiceHeader ")})
@RequestMapping("/invoiceheader")
@RestController
public class InvoiceHeaderController {
	
	@Autowired
	InvoiceHeaderService invoiceheaderService;
	
    @ApiOperation(response = InvoiceHeader.class, value = "Get all InvoiceHeader details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<AddInvoiceHeader> invoiceheaderList = invoiceheaderService.getInvoiceHeaders();
		return new ResponseEntity<>(invoiceheaderList, HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "invoiceNumber") String sortBy) {
		Page<InvoiceHeader> list = invoiceheaderService.getAllInvoiceHeaders(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Get a InvoiceHeader") // label for swagger 
	@GetMapping("/{invoiceNumber}")
	public ResponseEntity<?> getInvoiceHeader(@PathVariable String invoiceNumber) {
    	AddInvoiceHeader invoiceheader = invoiceheaderService.getInvoiceHeader(invoiceNumber);
    	log.info("InvoiceHeader : " + invoiceheader);
		return new ResponseEntity<>(invoiceheader, HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Get a InvoiceHeader") // label for swagger
	@GetMapping("/top")
	public ResponseEntity<?> getTopInvoiceHeader() {
    	AddInvoiceHeader invoiceHeader = invoiceheaderService.getLatestInvoiceHeader();
		log.info("AddInvoiceHeader : " + invoiceHeader);
		return new ResponseEntity<>(invoiceHeader, HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Get a InvoiceHeader") // label for swagger
   	@GetMapping("/top/qbQuery")
   	public ResponseEntity<?> getTopInvoiceHeaderForQBQuery() {
       	String invoiceNumber = invoiceheaderService.getTopRecordByStatusIdAndQbQuery();
   		log.info("invoiceNumber : " + invoiceNumber);
   		return new ResponseEntity<>(invoiceNumber, HttpStatus.OK);
   	}
    
	@ApiOperation(response = InvoiceHeader.class, value = "Search InvoiceHeader") // label for swagger
	@PostMapping("/findInvoiceHeader")
	public List<AddInvoiceHeader> findInvoiceHeader(@RequestBody SearchInvoiceHeader searchInvoiceHeader)
			throws Exception {
		return invoiceheaderService.findInvoiceHeader(searchInvoiceHeader);
	}
	
	@ApiOperation(response = InvoiceHeader.class, value = "Search MatterGenAcc") // label for swagger
	@GetMapping("/findRecords")
	public List<InvoiceHeader> findRecords(@RequestParam String fullTextSearch) throws ParseException {
		return invoiceheaderService.findRecords(fullTextSearch);
	}
	
	@ApiOperation(response = InvoicePreBillDetails.class, value = "Search PreBillDetails & Invoice Execute") // label for swagger
	@PostMapping("/invoiceExecute")
	public List<InvoicePreBillDetails> invoiceExecute(@RequestBody SearchPreBillDetails searchPreBillDetails)
			throws Exception {
		return invoiceheaderService.invoiceExecute(searchPreBillDetails);
	}
	
    @ApiOperation(response = AddInvoiceHeader[].class, value = "Create InvoiceHeader") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postInvoiceHeader(@Valid @RequestBody List<AddInvoiceHeader> newInvoiceHeader, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<AddInvoiceHeader> createdInvoiceHeader = 
				invoiceheaderService.createInvoiceHeader(newInvoiceHeader, loginUserID);
		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = AddInvoiceHeader.class, value = "Update InvoiceHeader") // label for swagger
    @PatchMapping("/{invoiceNumber}")
	public ResponseEntity<?> patchInvoiceHeader(@PathVariable String invoiceNumber, 
			@Valid @RequestBody UpdateInvoiceHeader updateInvoiceHeader, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		AddInvoiceHeader createdInvoiceHeader = 
				invoiceheaderService.updateInvoiceHeader(invoiceNumber, updateInvoiceHeader, loginUserID);
		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Update InvoiceHeader") // label for swagger
    @GetMapping("/{invoiceNumber}/qb")
	public ResponseEntity<?> patchInvoiceHeaderQB(@PathVariable String invoiceNumber, @RequestParam Long statusId) 
			throws IllegalAccessException, InvocationTargetException {
		InvoiceHeader updatedInvoiceHeader = invoiceheaderService.updateInvoiceHeaderQB(invoiceNumber, statusId);
		return new ResponseEntity<>(updatedInvoiceHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceHeader.class, value = "Delete InvoiceHeader") // label for swagger
	@DeleteMapping("/{invoiceNumber}")
	public ResponseEntity<?> deleteInvoiceHeader(@PathVariable String invoiceNumber, @RequestParam String loginUserID) throws IllegalAccessException {
    	invoiceheaderService.deleteInvoiceHeader(invoiceNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //--------------------------------TransferBilling---------------------------------------------------------
    @ApiOperation(response = InvoiceHeader.class, value = "Update InvoiceHeader") // label for swagger
    @GetMapping("/transferBilling")
	public ResponseEntity<?> transferMatterBilling(@RequestParam String fromMatterNumber, @RequestParam String toMatterNumber,
			@RequestParam(required = false) String fromDateRange, @RequestParam(required = false) String toDateRange) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		TransferBilling transferBilling = invoiceheaderService.transferBilling(fromMatterNumber, toMatterNumber, fromDateRange, toDateRange);
		log.info("transferBilling: " + transferBilling);
		return new ResponseEntity<>(transferBilling , HttpStatus.OK);
	}
    
    //---------------------------------Payment Update---------------------------------------------------------
    
    @ApiOperation(response = PaymentUpdate.class, value = "Create PaymentUpdate") // label for swagger
    @PostMapping("/paymentUpdate")
	public ResponseEntity<?> createPaymentUpdateByInvoiceRet(@RequestBody InvoiceRet invoiceRet)
			throws IllegalAccessException, InvocationTargetException, Exception {
		List<PaymentUpdate> createdPaymentUpdate = invoiceheaderService.createPaymentUpdate(invoiceRet);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentUpdate.class, value = "Create PaymentUpdate") // label for swagger
    @PostMapping("/paymentUpdate/create")
	public ResponseEntity<?> createPaymentUpdateTable(@RequestBody PaymentUpdate paymentUpdate, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, Exception {
		PaymentUpdate createdPaymentUpdate = invoiceheaderService.createPaymentUpdateTable(paymentUpdate, loginUserID);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentUpdate.class, value = "Update PaymentUpdate") // label for swagger
    @PatchMapping("/paymentUpdate/{paymentId}")
	public ResponseEntity<?> patchPaymentUpdate(@PathVariable Long paymentId, 
			@Valid @RequestBody PaymentUpdate paymentUpdate) 
			throws IllegalAccessException, InvocationTargetException {
		PaymentUpdate createdPaymentUpdate = invoiceheaderService.updatePaymentUpdate(paymentId, paymentUpdate);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
    
	@ApiOperation(response = PaymentUpdate.class, value = "Get PaymentUpdate") // label for swagger
	@GetMapping("/paymentUpdate/{paymentId}")
	public ResponseEntity<?> getPaymentUpdate(@PathVariable Long paymentId)
			throws IllegalAccessException, InvocationTargetException {
		PaymentUpdate getPaymentUpdate = invoiceheaderService.getPaymentUpdate(paymentId);
		return new ResponseEntity<>(getPaymentUpdate , HttpStatus.OK);
	}
	
	@ApiOperation(response = PaymentUpdate.class, value = "Search PaymentUpdate") // label for swagger
	@PostMapping("/findPaymentUpdate")
	public List<PaymentUpdate> findPaymentUpdate(@RequestBody SearchPaymentUpdate searchPaymentUpdate)
			throws Exception {
		return invoiceheaderService.findPaymentUpdate(searchPaymentUpdate);
	}
	
	@ApiOperation(response = PaymentUpdate.class, value = "Delete PaymentUpdate") // label for swagger
	@DeleteMapping("/paymentUpdate/{paymentId}")
	public ResponseEntity<?> deletePaymentUpdate(@PathVariable Long paymentId)
			throws IllegalAccessException, InvocationTargetException {
		invoiceheaderService.deletePaymentUpdate(paymentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    @ApiOperation(response = PaymentUpdate.class, value = "Update InvoiceHeader") // label for swagger
    @PostMapping("/paymentUpdate/receivePayment")
	public ResponseEntity<?> createPaymentUpdateByReceivePayment(@RequestBody List<ReceivePaymentResponse> receivePaymentResponseList)
			throws IllegalAccessException, InvocationTargetException, Exception {
		List<PaymentUpdate> createdPaymentUpdate = invoiceheaderService.createPaymentUpdateByReceivePayment(receivePaymentResponseList);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
    
    /*
     * ---------------------------------REPORTS--------------------------------------------------------------------------------
     */
    
    //------------------------ARAgingReport------------------------------------------------------------
    @ApiOperation(response = ARAgingReport[].class, value = "Create ARAgingReport") // label for swagger
   	@PostMapping("/arAgingReport")
   	public ResponseEntity<?> postARAgingReport(@Valid @RequestBody ARAgingReportInput arAgingReportInput)
   			throws IllegalAccessException, InvocationTargetException, Exception {
   		List<ARAgingReport> createdInvoiceHeader = invoiceheaderService.createARAgingReport(arAgingReportInput);
   		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
   	}
    
    //------------------------BillingReport------------------------------------------------------------
    @ApiOperation(response = BillingReport[].class, value = "Create BillingReport") // label for swagger
   	@PostMapping("/billingReport")
   	public ResponseEntity<?> postBillingReport(@Valid @RequestBody BillingReportInput billingReportInput)
   			throws Exception {
   		List<BillingReport> createdInvoiceHeader = invoiceheaderService.createBillingReport(billingReportInput);
   		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
   	}

	//------------------------PartnerBillingReport------------------------------------------------------------
	@ApiOperation(response = PartnerBillingReportImpl.class, value = "Create PartnerBillingReport") // label for swagger
	@PostMapping("/partnerBillingReport")
	public ResponseEntity<?> postPartnerBillingReport(@Valid @RequestBody BillingReportInput billingReportInput)
			throws Exception {
		List<PartnerBillingReportImpl> createdPartnerBillingReport = invoiceheaderService.createPartnerBillingReport(billingReportInput);
		return new ResponseEntity<>(createdPartnerBillingReport , HttpStatus.OK);
	}

	//------------------------Matter-Billing-Activity-----------------------------------------------------------
    @ApiOperation(response = MatterBillingActvityReport.class, value = "Create BillingReport") // label for swagger
   	@PostMapping("/matterBillingActivityReport")
   	public ResponseEntity<?> postMatterBillingActivityReport(@Valid @RequestBody MatterBillingActvityReportInput billingReportInput)
   			throws Exception {
   		MatterBillingActvityReport createdInvoiceHeader = invoiceheaderService.createBillingActivityReport(billingReportInput);
   		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
   	}

	//------------------------Matter-P&L Report-----------------------------------------------------------
	@ApiOperation(response = MatterPLReport.class, value = "Create Matter P&L Report") // label for swagger
	@PostMapping("/matterPandLReport")
	public ResponseEntity<?> postMatterPandLReport(@Valid @RequestBody MatterPLReportInput matterPLReportInput)
			throws Exception {
		List<MatterPLReport> createdMatterPLReport = invoiceheaderService.createMatterPLReport(matterPLReportInput);
		return new ResponseEntity<>(createdMatterPLReport , HttpStatus.OK);
	}

	//------------------------Immigration Payment Plan Report-----------------------------------------------------------
	@ApiOperation(response = ImmigrationPaymentPlanReport.class, value = "Create Immigration Payment Plan Report") // label for swagger
	@PostMapping("/immigrationPaymentPlanReport")
	public ResponseEntity<?> postImmigrationPaymentPlan(@Valid @RequestBody ImmigrationPaymentPlanReportInput immigrationPaymentPlanReportInput)
			throws Exception {
		List<ImmigrationPaymentPlanReport> createdImmigrationPaymentPlanReport = invoiceheaderService.createImmigrationPaymentPlanReport(immigrationPaymentPlanReportInput);
		return new ResponseEntity<>(createdImmigrationPaymentPlanReport , HttpStatus.OK);
	}
}