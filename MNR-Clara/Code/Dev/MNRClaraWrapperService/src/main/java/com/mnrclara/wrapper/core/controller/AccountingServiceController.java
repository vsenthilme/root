package com.mnrclara.wrapper.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.wrapper.core.model.accounting.ARReport;
import com.mnrclara.wrapper.core.model.accounting.AddInvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.AddInvoiceLine;
import com.mnrclara.wrapper.core.model.accounting.BillByGroup;
import com.mnrclara.wrapper.core.model.accounting.BilledHoursPaid;
import com.mnrclara.wrapper.core.model.accounting.BilledHoursReport;
import com.mnrclara.wrapper.core.model.accounting.BilledIncomeDashboard;
import com.mnrclara.wrapper.core.model.accounting.BilledPaidFees;
import com.mnrclara.wrapper.core.model.accounting.BilledPaidFeesReport;
import com.mnrclara.wrapper.core.model.accounting.BilledUnBilledHours;
import com.mnrclara.wrapper.core.model.accounting.BilledUnBilledHoursReport;
import com.mnrclara.wrapper.core.model.accounting.CaseAssignmentDashboard;
import com.mnrclara.wrapper.core.model.accounting.ClientCashReceipts;
import com.mnrclara.wrapper.core.model.accounting.ClientCashReceiptsReport;
import com.mnrclara.wrapper.core.model.accounting.ClientIncomeSummaryReport;
import com.mnrclara.wrapper.core.model.accounting.ExpirationDateReport;
import com.mnrclara.wrapper.core.model.accounting.ExpirationDateRequest;
import com.mnrclara.wrapper.core.model.accounting.InvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.InvoicePreBillDetails;
import com.mnrclara.wrapper.core.model.accounting.InvoiceRet;
import com.mnrclara.wrapper.core.model.accounting.MatterBillingActvityReport;
import com.mnrclara.wrapper.core.model.accounting.MatterBillingActvityReportInput;
import com.mnrclara.wrapper.core.model.accounting.MatterListingReport;
import com.mnrclara.wrapper.core.model.accounting.MatterTimeExpenseTicket;
import com.mnrclara.wrapper.core.model.accounting.PaymentPlanHeader;
import com.mnrclara.wrapper.core.model.accounting.PaymentPlanLine;
import com.mnrclara.wrapper.core.model.accounting.PaymentUpdate;
import com.mnrclara.wrapper.core.model.accounting.PreBillApproveSaveDetails;
import com.mnrclara.wrapper.core.model.accounting.PreBillDetails;
import com.mnrclara.wrapper.core.model.accounting.PreBillOutputForm;
import com.mnrclara.wrapper.core.model.accounting.QuotationHeader;
import com.mnrclara.wrapper.core.model.accounting.QuotationLine;
import com.mnrclara.wrapper.core.model.accounting.SearchAR;
import com.mnrclara.wrapper.core.model.accounting.SearchInvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.SearchPaymentPlanHeader;
import com.mnrclara.wrapper.core.model.accounting.SearchPaymentUpdate;
import com.mnrclara.wrapper.core.model.accounting.SearchPreBillDetails;
import com.mnrclara.wrapper.core.model.accounting.SearchQuotationHeader;
import com.mnrclara.wrapper.core.model.accounting.TransferBilling;
import com.mnrclara.wrapper.core.model.accounting.UpdateInvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.WriteOffReport;
import com.mnrclara.wrapper.core.model.report.*;
import com.mnrclara.wrapper.core.model.report.ARAgingReportInput;
import com.mnrclara.wrapper.core.model.report.BillingReport;
import com.mnrclara.wrapper.core.model.report.BillingReportInput;
import com.mnrclara.wrapper.core.model.report.SearchMatterListing;
import com.mnrclara.wrapper.core.service.AccountingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-accounting-service")
@Api(tags = {"Accounting Service"}, value = "Accounting Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Accounting Modules")})
public class AccountingServiceController { 
	
	@Autowired
	AccountingService accountingService;

	/*
	 * --------------------------------QuotationHeader---------------------------------
	 */

	@ApiOperation(response = QuotationHeader.class, value = "Get all QuotationHeader details") // label for swagger
	@GetMapping("/quotationheader")
	public ResponseEntity<?> getQuotationHeaders(@RequestParam String authToken) {
		QuotationHeader[] quotationNoList = accountingService.getQuotationHeaders(authToken);
		return new ResponseEntity<>(quotationNoList, HttpStatus.OK);
	}

	@ApiOperation(response = QuotationHeader.class, value = "Get all QuotationHeader details") // label for swagger
	@GetMapping("/quotationheader/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "quotationNo") String sortBy,
									@RequestParam String authToken) {
		Page<QuotationHeader> list = accountingService.getAllQuotationHeaders(pageNo, pageSize, sortBy, authToken);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
	
	@ApiOperation(response = QuotationHeader.class, value = "Get a QuotationHeader") // label for swagger
	@GetMapping("/quotationheader/{quotationNo}")
	public ResponseEntity<?> getQuotationHeader(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo,
			@RequestParam String authToken) {
		QuotationHeader dbQuotationHeader = accountingService.getQuotationHeader(quotationNo, quotationRevisionNo, authToken);
		log.info("QuotationHeader : " + dbQuotationHeader);
		return new ResponseEntity<>(dbQuotationHeader, HttpStatus.OK);
	}
	
	@ApiOperation(response = QuotationHeader.class, value = "Search QuotationHeader") // label for swagger
	@PostMapping("/quotationheader/findQuotationHeader")
	public ResponseEntity<?> findQuotationHeader(@RequestBody SearchQuotationHeader searchQuotationHeader, @RequestParam String authToken)
			throws Exception {
		QuotationHeader[] quotationHeaderResults = accountingService.findQuotationHeaders(searchQuotationHeader, authToken);
		return new ResponseEntity<>(quotationHeaderResults, HttpStatus.OK);
	}

	@ApiOperation(response = QuotationHeader.class, value = "Create QuotationHeader") // label for swagger
	@PostMapping("/quotationheader")
	public ResponseEntity<?> postQuotationHeader(@Valid @RequestBody QuotationHeader newQuotationHeader, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		QuotationHeader createdQuotationHeader = accountingService.createQuotationHeader(newQuotationHeader, loginUserID, authToken);
		return new ResponseEntity<>(createdQuotationHeader, HttpStatus.OK);
	}

	@ApiOperation(response = QuotationHeader.class, value = "Update QuotationHeader") // label for swagger
	@RequestMapping(value = "/quotationheader/{quotationNo}", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchQuotationHeader(@PathVariable String quotationNo,  @RequestParam Long quotationRevisionNo, 
			@RequestParam String loginUserID, @RequestParam String authToken, @Valid @RequestBody QuotationHeader updateQuotationHeader)
			throws IllegalAccessException, InvocationTargetException {
		QuotationHeader updatedQuotationHeader = accountingService.updateQuotationHeader(quotationNo, quotationRevisionNo, loginUserID, updateQuotationHeader, authToken);
		return new ResponseEntity<>(updatedQuotationHeader, HttpStatus.OK);
	}

	@ApiOperation(response = QuotationHeader.class, value = "Delete QuotationHeader") // label for swagger
	@DeleteMapping("/quotationheader/{quotationNo}")
	public ResponseEntity<?> deleteQuotationHeader(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		accountingService.deleteQuotationHeader(quotationNo, quotationRevisionNo, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * --------------------------------QuotationLine---------------------------------
	 */
	@ApiOperation(response = QuotationLine.class, value = "Get QuotationLines") // label for swagger
	@GetMapping("/quotationline/{quotationNo}")
	public ResponseEntity<?> getQuotationLine(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo, @RequestParam String authToken) {
		QuotationLine[] dbQuotationLine = accountingService.getQuotationLines(quotationNo, quotationRevisionNo, authToken);
		log.info("QuotationLine : " + dbQuotationLine);
		return new ResponseEntity<>(dbQuotationLine, HttpStatus.OK);
	}
	
	@ApiOperation(response = QuotationLine.class, value = "Get a QuotationLine") // label for swagger 
	@GetMapping("/quotationline/{quotationNo}/{quotationRevisionNo}/{serialNo}")
	public ResponseEntity<?> getQuotationLine(@PathVariable String quotationNo, @PathVariable Long quotationRevisionNo, 
			@PathVariable Long serialNo, @RequestParam String authToken) {
    	QuotationLine quotationline = accountingService.getQuotationLine(quotationNo, quotationRevisionNo, serialNo, authToken);
    	log.info("QuotationLine : " + quotationline);
		return new ResponseEntity<>(quotationline, HttpStatus.OK);
	}
	
	@ApiOperation(response = QuotationLine.class, value = "Update QuotationLine") // label for swagger
    @PatchMapping("/quotationline/{quotationNo}")
	public ResponseEntity<?> patchQuotationLine(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo, 
			@RequestParam Long serialNo, @Valid @RequestBody QuotationLine updateQuotationLine, 
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		QuotationLine createdQuotationLine = accountingService.updateQuotationLine(quotationNo, quotationRevisionNo, serialNo, updateQuotationLine, loginUserID, authToken);
		return new ResponseEntity<>(createdQuotationLine , HttpStatus.OK);
	}
	
	/*
	 * --------------------------------PaymentPlanHeader---------------------------------
	 */
	@ApiOperation(response = PaymentPlanHeader.class, value = "Get all PaymentPlanHeader details") // label for swagger
	@GetMapping("/paymentplanheader")
	public ResponseEntity<?> getPaymentPlanHeaders(@RequestParam String authToken) {
		PaymentPlanHeader[] paymentPlanNumberList = accountingService.getPaymentPlanHeaders(authToken);
		return new ResponseEntity<>(paymentPlanNumberList, HttpStatus.OK);
	}
	
	@ApiOperation(response = PaymentPlanHeader.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/paymentplanheader/pagination")
	public ResponseEntity<?> getAllPaymentPlanHeaders(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "paymentPlanNumber") String sortBy,
									@RequestParam String authToken) {
		Page<PaymentPlanHeader> list = accountingService.getAllPaymentPlanHeaders(pageNo, pageSize, sortBy, authToken);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}

	@ApiOperation(response = PaymentPlanHeader.class, value = "Get a PaymentPlanHeader") // label for swagger
	@GetMapping("/paymentplanheader/{paymentPlanNumber}")
	public ResponseEntity<?> getPaymentPlanHeader(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam String authToken) {
		PaymentPlanHeader dbPaymentPlanHeader = accountingService.getPaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo, authToken);
		log.info("PaymentPlanHeader : " + dbPaymentPlanHeader);
		return new ResponseEntity<>(dbPaymentPlanHeader, HttpStatus.OK);
	}
	
	@ApiOperation(response = PaymentPlanHeader.class, value = "Search PaymentPlanHeader") // label for swagger
	@PostMapping("/paymentplanheader/findPaymentPlanHeader")
	public PaymentPlanHeader[] findPaymentPlanHeader(@RequestBody SearchPaymentPlanHeader searchPaymentPlanHeader, 
			@RequestParam String authToken)
			throws Exception {
		return accountingService.findPaymentPlanHeader(searchPaymentPlanHeader, authToken);
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Send SMS") // label for swagger
   	@GetMapping("/paymentplanheader/{paymentPlanNumber}/reminderSMS")
   	public ResponseEntity<?> sendReminderSMS() 
   			throws IllegalAccessException, InvocationTargetException {
   		Boolean smsResponse = accountingService.sendReminderSMS();
   		return new ResponseEntity<>(smsResponse , HttpStatus.OK);
   	}

	@ApiOperation(response = PaymentPlanHeader.class, value = "Create PaymentPlanHeader") // label for swagger
	@PostMapping("/paymentplanheader")
	public ResponseEntity<?> postPaymentPlanHeader(@Valid @RequestBody PaymentPlanHeader newPaymentPlanHeader, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		PaymentPlanHeader createdPaymentPlanHeader = accountingService.createPaymentPlanHeader(newPaymentPlanHeader, loginUserID, authToken);
		return new ResponseEntity<>(createdPaymentPlanHeader, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanHeader.class, value = "Update PaymentPlanHeader") // label for swagger
	@RequestMapping(value = "/paymentplanheader", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchPaymentPlanHeader(@RequestParam String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam String loginUserID, @RequestParam String authToken, @Valid @RequestBody PaymentPlanHeader updatePaymentPlanHeader)
			throws IllegalAccessException, InvocationTargetException {
		PaymentPlanHeader updatedPaymentPlanHeader = 
				accountingService.updatePaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo, loginUserID, updatePaymentPlanHeader, authToken);
		return new ResponseEntity<>(updatedPaymentPlanHeader, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanHeader.class, value = "Delete PaymentPlanHeader") // label for swagger
	@DeleteMapping("/paymentplanheader/{paymentPlanNumber}")
	public ResponseEntity<?> deletePaymentPlanHeader(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		accountingService.deletePaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * --------------------------------PaymentPlanLine--------------------------------------------------------------------------
	 */
	@ApiOperation(response = PaymentPlanLine.class, value = "Get all PaymentPlanLine details") // label for swagger
	@GetMapping("/paymentplanline")
	public ResponseEntity<?> getPaymentPlanLines(@RequestParam String authToken) {
		PaymentPlanLine[] paymentPlanRevisionNoList = accountingService.getPaymentPlanLines(authToken);
		return new ResponseEntity<>(paymentPlanRevisionNoList, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanLine.class, value = "Get a PaymentPlanLine") // label for swagger
	@GetMapping("/paymentplanline/{paymentPlanNumber}")
	public ResponseEntity<?> getPaymentPlanLine(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam Long itemNumber, @RequestParam String authToken) {
		PaymentPlanLine dbPaymentPlanLine = accountingService.getPaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber, authToken);
		log.info("PaymentPlanLine : " + dbPaymentPlanLine);
		return new ResponseEntity<>(dbPaymentPlanLine, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanLine.class, value = "Create PaymentPlanLine") // label for swagger
	@PostMapping("/paymentplanline")
	public ResponseEntity<?> postPaymentPlanLine(@Valid @RequestBody PaymentPlanLine newPaymentPlanLine, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		PaymentPlanLine createdPaymentPlanLine = accountingService.createPaymentPlanLine(newPaymentPlanLine, loginUserID, authToken);
		return new ResponseEntity<>(createdPaymentPlanLine, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanLine.class, value = "Update PaymentPlanLine") // label for swagger
	@RequestMapping(value = "/paymentplanline", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchPaymentPlanLine(@RequestParam String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam Long itemNumber, @RequestParam String loginUserID, @RequestParam String authToken,
			@Valid @RequestBody PaymentPlanLine updatePaymentPlanLine)
			throws IllegalAccessException, InvocationTargetException {
		PaymentPlanLine updatedPaymentPlanLine = 
				accountingService.updatePaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber, loginUserID, updatePaymentPlanLine, authToken);
		return new ResponseEntity<>(updatedPaymentPlanLine, HttpStatus.OK);
	}

	@ApiOperation(response = PaymentPlanLine.class, value = "Delete PaymentPlanLine") // label for swagger
	@DeleteMapping("/paymentplanline/{paymentPlanNumber}")
	public ResponseEntity<?> deletePaymentPlanLine(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam Long itemNumber, @RequestParam String loginUserID, @RequestParam String authToken) {
		accountingService.deletePaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//------------------------------------PRE-BILL----------------------------------------------------------------
	@ApiOperation(response = PreBillDetails.class, value = "Get all PreBillDetails details") // label for swagger
	@GetMapping("/prebilldetails/preBillNumber")
	public ResponseEntity<?> getPreBillDetailss(@RequestParam String authToken) {
		PreBillDetails[] preBillNumberList = accountingService.getPreBillDetails(authToken);
		return new ResponseEntity<>(preBillNumberList, HttpStatus.OK);
	}
	
	@ApiOperation(response = PreBillDetails.class, value = "Get all PreBillDetails details") // label for swagger
	@GetMapping("/prebilldetails/pagination")
	public ResponseEntity<?> getAllPreBillDetailss(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "preBillNumber") String sortBy,
									@RequestParam String authToken) {
		Page<PreBillDetails> list = accountingService.getAllPreBillDetailss(pageNo, pageSize, sortBy, authToken);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}

	@ApiOperation(response = PreBillDetails.class, value = "Get a PreBillDetails") // label for swagger
	@GetMapping("/prebilldetails/{preBillNumber}")
	public ResponseEntity<?> getPreBillDetails(@PathVariable String preBillNumber, @RequestParam String authToken) {
		PreBillDetails dbPreBillDetails = accountingService.getPreBillDetails(preBillNumber, authToken);
		log.info("PreBillDetails : " + dbPreBillDetails);
		return new ResponseEntity<>(dbPreBillDetails, HttpStatus.OK);
	}
	
	@ApiOperation(response = PreBillDetails.class, value = "Search PreBillDetails") // label for swagger
	@PostMapping("/prebilldetails/findPreBillDetails")
	public PreBillDetails[] findPreBillDetails(@RequestBody SearchPreBillDetails searchPreBillDetails, @RequestParam String authToken)
			throws Exception {
		log.info("searchPreBillDetails : " + searchPreBillDetails);
		return accountingService.findPreBillDetails(searchPreBillDetails, authToken);
	}

	@ApiOperation(response = PreBillDetails.class, value = "Create PreBillDetails") // label for swagger
	@PostMapping("/prebilldetails")
	public ResponseEntity<?> postPreBillDetails(@Valid @RequestBody List<PreBillDetails> newPreBillDetails, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		PreBillDetails[] createdPreBillDetails = 
				accountingService.createPreBillDetails(newPreBillDetails, loginUserID, authToken);
		return new ResponseEntity<>(createdPreBillDetails, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTimeExpenseTicket[].class, value = "Execute Bill") // label for swagger
   	@PostMapping("/prebilldetails/executeBill")
   	public ResponseEntity<?> executeBill(@Valid @RequestBody BillByGroup newBillByGroup, @RequestParam Boolean isByIndividual,
   			@RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException, ParseException {
    	MatterTimeExpenseTicket[] arrMatterTimeExpenseTicket = accountingService.executeBill(newBillByGroup, isByIndividual, authToken);
   		return new ResponseEntity<>(arrMatterTimeExpenseTicket, HttpStatus.OK);
   	}

	@ApiOperation(response = PreBillDetails.class, value = "Approve PreBillDetails") // label for swagger
	@PostMapping("/prebilldetails/approve")
	public ResponseEntity<?> approvePreBillDetails(@Valid @RequestBody List<PreBillApproveSaveDetails> preBillApproveSaveDetails,
			@RequestParam String loginUserID, @RequestParam String authToken) {
		PreBillDetails[] response = accountingService.approvePreBillDetails(preBillApproveSaveDetails, loginUserID, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = PreBillDetails.class, value = "Save PreBillDetails") // label for swagger
	@PostMapping("/prebilldetails/save")
	public ResponseEntity<?> savePreBillDetails(@Valid @RequestBody List<PreBillApproveSaveDetails> preBillApproveSaveDetails, @RequestParam String loginUserID,
												@RequestParam String authToken) {
		PreBillDetails[] response = accountingService.savePreBillDetails(preBillApproveSaveDetails, loginUserID, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	    
	@ApiOperation(response = PreBillDetails.class, value = "Update PreBillDetails") // label for swagger
	@PatchMapping("/prebilldetails/{preBillBatchNumber}")
	public ResponseEntity<?> updatePreBillDetails(@PathVariable String preBillBatchNumber, @RequestBody PreBillDetails updatePreBillDetails, 
			@RequestParam String preBillNumber, @RequestParam Date preBillDate, @RequestParam String matterNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	PreBillDetails updatedPreBillDetails = accountingService.updatePreBillDetails(preBillBatchNumber, 
    			preBillNumber, preBillDate, matterNumber, loginUserID, updatePreBillDetails, authToken);
		return new ResponseEntity<>(updatedPreBillDetails, HttpStatus.NO_CONTENT);
	}
	 
	@ApiOperation(response = PreBillDetails.class, value = "Delete PreBillDetails") // label for swagger
	@DeleteMapping("/prebilldetails/{preBillNumber}")
	public ResponseEntity<?> deletePreBillDetails(@PathVariable String preBillNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		accountingService.deletePreBillDetails(preBillNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*------------------------------------------------------------------------------*/
	@ApiOperation(response = AddInvoiceHeader[].class, value = "Get all InvoiceHeader details") // label for swagger
	@GetMapping("/invoiceheader")
	public ResponseEntity<?> getInvoiceHeaders(@RequestParam String authToken) {
		AddInvoiceHeader[] invoiceheaderList = accountingService.getInvoiceHeaders(authToken);
		return new ResponseEntity<>(invoiceheaderList, HttpStatus.OK);
	}
	
	@ApiOperation(response = AddInvoiceHeader.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/invoiceheader/pagination")
	public ResponseEntity<?> getAllInvoiceHeaders (@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "invoiceNumber") String sortBy,
									@RequestParam String authToken) {
		Page<AddInvoiceHeader> list = accountingService.getAllInvoiceHeaders(pageNo, pageSize, sortBy, authToken);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
    
    @ApiOperation(response = AddInvoiceHeader.class, value = "Get a InvoiceHeader") // label for swagger 
	@GetMapping("/invoiceheader/{invoiceNumber}")
	public ResponseEntity<?> getInvoiceHeader(@PathVariable String invoiceNumber, @RequestParam String authToken) {
    	AddInvoiceHeader invoiceheader = accountingService.getInvoiceHeader(invoiceNumber, authToken);
    	log.info("InvoiceHeader : " + invoiceheader);
		return new ResponseEntity<>(invoiceheader, HttpStatus.OK);
	}
    
	@ApiOperation(response = AddInvoiceLine[].class, value = "Get all InvoiceLine details") // label for swagger
	@GetMapping("/invoiceline/{invoiceNumber}")
	public ResponseEntity<?> getInvoiceLines(@PathVariable String invoiceNumber, String authToken) {
		AddInvoiceLine[] invoiceheaderList = accountingService.getInvoiceLine(invoiceNumber, authToken);
		return new ResponseEntity<>(invoiceheaderList, HttpStatus.OK);
	}
    
	@ApiOperation(response = AddInvoiceHeader[].class, value = "Search InvoiceHeader") // label for swagger
	@PostMapping("/invoiceheader/findInvoiceHeader")
	public com.mnrclara.wrapper.core.model.accounting.spark.InvoiceHeader[] findInvoiceHeader(@RequestBody SearchInvoiceHeader searchInvoiceHeader,
			@RequestParam String authToken)
			throws Exception {
		return accountingService.findInvoiceHeader(searchInvoiceHeader, authToken);
	}
	
	@ApiOperation(response = AddInvoiceHeader[].class, value = "Create InvoiceHeader") // label for swagger
	@PostMapping("/invoiceheader")
	public ResponseEntity<?> postInvoiceHeader(@Valid @RequestBody List<InvoiceHeader> newInvoiceHeader, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		InvoiceHeader[] createdInvoiceHeader = 
				accountingService.createInvoiceHeader(newInvoiceHeader, loginUserID, authToken);
		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
	}
	
	@ApiOperation(response = AddInvoiceHeader.class, value = "Update InvoiceHeader") // label for swagger
    @PatchMapping("/invoiceheader/{invoiceNumber}")
	public ResponseEntity<?> patchInvoiceHeader(@PathVariable String invoiceNumber, 
			@Valid @RequestBody UpdateInvoiceHeader updateInvoiceHeader, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		AddInvoiceHeader createdInvoiceHeader = 
				accountingService.updateInvoiceHeader(invoiceNumber, updateInvoiceHeader, loginUserID, authToken);
		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = AddInvoiceHeader.class, value = "Delete InvoiceHeader") // label for swagger
	@DeleteMapping("/invoiceheader/{invoiceNumber}")
	public ResponseEntity<?> deleteInvoiceHeader(@PathVariable String invoiceNumber, @RequestParam String loginUserID,
			@RequestParam String authToken) { 
    	accountingService.deleteInvoiceHeader(invoiceNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    @ApiOperation(response = InvoicePreBillDetails[].class, value = "Search PreBillDetails & Invoice Execute") // label for swagger
	@PostMapping("/invoiceheader/invoiceExecute")
	public InvoicePreBillDetails[] invoiceExecute(@RequestBody SearchPreBillDetails searchPreBillDetails,
			@RequestParam String authToken) throws Exception {
		return accountingService.invoiceExecute(searchPreBillDetails, authToken);
	}
    
    //--------------------------------TransferBilling---------------------------------------------------------
    @ApiOperation(response = TransferBilling.class, value = "Update InvoiceHeader") // label for swagger
    @GetMapping("/invoiceheader/transferBilling")
	public ResponseEntity<?> transferMatterBilling(@RequestParam String fromMatterNumber, @RequestParam String toMatterNumber,
			@RequestParam(required = false) String fromDateRange, @RequestParam(required = false) String toDateRange,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException, ParseException {
		TransferBilling transferBilling = 
				accountingService.transferBilling(fromMatterNumber, toMatterNumber, fromDateRange, toDateRange, authToken);
		return new ResponseEntity<>(transferBilling , HttpStatus.OK);
	}
    
	//---------------------PaymentUpdate----------------------------------------------------------------------
	
	@ApiOperation(response = PaymentUpdate.class, value = "Search PaymentUpdate") // label for swagger
	@PostMapping("/invoiceheader/findPaymentUpdate")
	public PaymentUpdate[] findPaymentUpdate(@RequestBody SearchPaymentUpdate searchPaymentUpdate,
			@RequestParam String authToken) throws Exception {
		return accountingService.findPaymentUpdate(searchPaymentUpdate, authToken);
	}
	
	@ApiOperation(response = PaymentUpdate.class, value = "Update InvoiceHeader") // label for swagger
    @PostMapping("/invoiceheader/paymentUpdate")
	public ResponseEntity<?> createPaymentUpdate(@RequestBody InvoiceRet invoiceRet,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException, Exception {
		com.mnrclara.wrapper.core.model.accounting.PaymentUpdate[] createdPaymentUpdate = 
				accountingService.createPaymentUpdate(invoiceRet, authToken);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
	
	@ApiOperation(response = PaymentUpdate.class, value = "Create PaymentUpdate") // label for swagger
    @PostMapping("/invoiceheader/paymentUpdate/create")
	public ResponseEntity<?> createPaymentUpdateTable(@RequestBody PaymentUpdate paymentUpdate, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException, Exception {
		PaymentUpdate createdPaymentUpdate = accountingService.createPaymentUpdateTable(paymentUpdate, loginUserID, authToken);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentUpdate.class, value = "Update PaymentUpdate") // label for swagger
    @PatchMapping("invoiceheader/paymentUpdate/{paymentId}")
	public ResponseEntity<?> patchPaymentUpdate(@PathVariable Long paymentId, 
			@Valid @RequestBody PaymentUpdate paymentUpdate, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		PaymentUpdate createdPaymentUpdate = accountingService.updatePaymentUpdate(paymentId, paymentUpdate, authToken);
		return new ResponseEntity<>(createdPaymentUpdate , HttpStatus.OK);
	}
	//Get
	@ApiOperation(response = PaymentUpdate.class, value = "Get PaymentUpdate") // label for swagger
	@GetMapping("invoiceheader/paymentUpdate/{paymentId}")
	public ResponseEntity<?> getPaymentUpdate(@PathVariable Long paymentId,
												@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		PaymentUpdate getPaymentUpdate = accountingService.getPaymentUpdate(paymentId, authToken);
		return new ResponseEntity<>(getPaymentUpdate , HttpStatus.OK);
	}
	//Delete
	@ApiOperation(response = PaymentUpdate.class, value = "Delete PaymentUpdate") // label for swagger
	@DeleteMapping("invoiceheader/paymentUpdate/{paymentId}")
	public ResponseEntity<?> deletePaymentUpdate(@PathVariable Long paymentId,
												 @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		accountingService.deletePaymentUpdate(paymentId, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//---------------------------------------------------------------------------------------------------------
	
    /*
     * Output Forms
     */
    @ApiOperation(response = PreBillOutputForm.class, value = "Get a PREBILLDETAILS OutputForms") // label for swagger 
	@GetMapping("/outputforms/preBill")
	public ResponseEntity<?> getPreBillDetails(@RequestParam String preBillNumber, @RequestParam String matterNumber,
			@RequestParam String authToken) {
    	PreBillOutputForm preBillOutputForm = 
    			accountingService.getPreBillDetailsOutputForm(preBillNumber, matterNumber, authToken);
		return new ResponseEntity<>(preBillOutputForm, HttpStatus.OK);
	}
    
    @ApiOperation(response = PreBillOutputForm.class, value = "Get a INVOICE OutputForms") // label for swagger 
    @GetMapping("/outputforms/invoice")
   	public ResponseEntity<?> getInvoiceDetails(@RequestParam String invoiceNumber, @RequestParam String authToken) {
       	PreBillOutputForm preBillOutputForm = accountingService.getInvoiceOutputForm(invoiceNumber, authToken);
   		return new ResponseEntity<>(preBillOutputForm, HttpStatus.OK);
   	}
    
    /*
     * ---------------------------Reports-------------------------------------------------------
     */
    @ApiOperation(response = ARAgingReport[].class, value = "Create ARAgingReport") // label for swagger
   	@PostMapping("/invoiceheader/arAgingReport")
   	public ResponseEntity<?> postARAgingReport(@Valid @RequestBody ARAgingReportInput arAgingReportInput,
   			@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException, ParseException {
   		ARAgingReport[] arAgingReportList = accountingService.createARAgingReport(arAgingReportInput, authToken);
   		log.info("arAgingReportList : " + Arrays.asList(arAgingReportList));
   		return new ResponseEntity<>(arAgingReportList , HttpStatus.OK);
   	}
    
    @ApiOperation(response = BillingReport[].class, value = "Create BillingReport") // label for swagger
   	@PostMapping("/invoiceheader/billingReport")
   	public ResponseEntity<?> postBillingReport(@Valid @RequestBody BillingReportInput billingReportInput,
   			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException, ParseException {
   		BillingReport[] createdInvoiceHeader = accountingService.createBillingReport(billingReportInput, authToken);
   		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
   	}

	//------------------------Matter-PL Report-----------------------------------------------------------
	@ApiOperation(response = MatterPLReport[].class, value = "Create Matter PL Report") // label for swagger
	@PostMapping("/invoiceheader/matterPLReport")
	public ResponseEntity<?> postMatterPLReport(@Valid @RequestBody MatterPLReportInput matterPLReportInput,
											   @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterPLReport[] createdMatterPLReport = accountingService.createMatterPLReport(matterPLReportInput, authToken);
		return new ResponseEntity<>(createdMatterPLReport , HttpStatus.OK);
	}

	//------------------------Immigration Payment Plan Report-----------------------------------------------------------
	@ApiOperation(response = ImmigrationPaymentPlanReport[].class, value = "Create Immigration Payment Plan Report") // label for swagger
	@PostMapping("/invoiceheader/immigrationPaymentPlanReport")
	public ResponseEntity<?> postImmigrationPaymentPlanReport(@Valid @RequestBody ImmigrationPaymentPlanReportInput immigrationPaymentPlanReportInput,
												@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException, ParseException {
		ImmigrationPaymentPlanReport[] createdImmigrationPaymentPlanReport = accountingService.createImmigrationPaymentPlanReport(immigrationPaymentPlanReportInput, authToken);
		return new ResponseEntity<>(createdImmigrationPaymentPlanReport , HttpStatus.OK);
	}

    //------------------------Matter-Billing-Activity-----------------------------------------------------------
    @ApiOperation(response = MatterBillingActvityReport[].class, value = "Create MatterBillingActvity Report") // label for swagger
   	@PostMapping("/invoiceheader/matterBillingActivityReport")
   	public ResponseEntity<?> postMatterBillingActivityReport(@Valid @RequestBody MatterBillingActvityReportInput billingReportInput,
   			@RequestParam String authToken)
   			throws Exception {
   		MatterBillingActvityReport createdInvoiceHeader = accountingService.createBillingActivityReport(billingReportInput, authToken);
   		return new ResponseEntity<>(createdInvoiceHeader , HttpStatus.OK);
   	}
    
    //----------------------Dahsboards--------------------------------------------------------------------------
    
    /*
     * Dashboard - Billed Income
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/dashboard/billedIncome")
	public ResponseEntity<?> billedIncome (@RequestParam Long classId, @RequestParam String period,
			@RequestParam String authToken) throws Exception {
		BilledIncomeDashboard dashboard = accountingService.generateBilledIncomeDashboard(classId, period, authToken);
    	return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}
    
    /*
     * Dashboard - Case Assignment
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/dashboard/caseAssignment")
	public ResponseEntity<?> caseAssignment (@RequestParam Long classId, 
			@RequestParam(defaultValue = "ResponsibleTimekeepers") String timeKeepers,
			@RequestParam String authToken) throws Exception {
		CaseAssignmentDashboard dashboard = accountingService.getCaseAssignmentDashboard(classId, timeKeepers, authToken);
    	return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - Billable UnBillable Time") // label for swagger
	@GetMapping("/dashboard/billableUnbillableTime")
	public ResponseEntity<?> getBillableNonBillableTime(@RequestParam Long classId, @RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) 
	String period, @RequestParam String authToken) {
		BilledIncomeDashboard dashboard = accountingService.getBillableNonBillableTime(classId,period, authToken);
		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - Client Referral") // label for swagger
	@GetMapping("/dashboard/clientReferral")
	public ResponseEntity<?> getClientReferral(@RequestParam Long classId,@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period, @RequestParam String authToken) {
		BilledIncomeDashboard dashboard = accountingService.getClientReferral(classId,period, authToken);
		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - Practice Breakdown") // label for swagger
	@GetMapping("/dashboard/practiceBreakDown")
	public ResponseEntity<?> getPracticeBreakDown(@RequestParam Long classId,@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period, @RequestParam String authToken) {
		BilledIncomeDashboard dashboard = accountingService.getPracticeBreakDown(classId,period, authToken);
		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - Top Clients") // label for swagger
	@GetMapping("/dashboard/topClients")
	public ResponseEntity<?> getTopClients(@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period, @RequestParam String authToken) {
		BilledIncomeDashboard dashboard = accountingService.getTopClients(period, authToken);
		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - Lead Conversion") // label for swagger
	@GetMapping("/dashboard/leadConversion")
	public ResponseEntity<?> getLeadConversion(@RequestParam Long classId,@RequestParam(name = "period", defaultValue = "CURRENTMONTH" ) String period, @RequestParam String authToken) {
		BilledIncomeDashboard dashboard = accountingService.getLeadConversion(classId,period, authToken);
		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - MATTER_LISTING") // label for swagger
	@PostMapping("/dashboard/matter-listing")
	public ResponseEntity<?> getMatterListing (@RequestBody SearchMatterListing searchMatterListing, @RequestParam String authToken) {
		MatterListingReport[] matterListing = accountingService.getMatterListing(searchMatterListing, authToken);
		return new ResponseEntity<>(matterListing, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - MATTER_RATES_LISTING") // label for swagger
	@PostMapping("/dashboard/matter-rates-listing")
	public ResponseEntity<?> getMatterRatesListing (@RequestBody SearchMatterListing searchMatterListing, @RequestParam String authToken) {
		MatterListingReport[] matterListing = accountingService.getMatterRatesListing(searchMatterListing, authToken);
		return new ResponseEntity<>(matterListing, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard - BILLED_UNBILLED_HOURS") // label for swagger
	@PostMapping("/dashboard/billed-unbilled-hours")
	public ResponseEntity<?> getBilledUnBilledHours (@RequestBody BilledUnBilledHours billedUnBilledHours, @RequestParam String authToken) throws ParseException {
		BilledUnBilledHoursReport[] billedUnBilledHoursData = accountingService.getBilledUnBilledHours(billedUnBilledHours, authToken);
		return new ResponseEntity<>(billedUnBilledHoursData, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Client Cash Receipts Report") // label for swagger
	@PostMapping("/dashboard/client-cash-receipt-report")
	public ResponseEntity<?> getClientCashReceipts (@RequestBody ClientCashReceipts clientCashReceipts, @RequestParam String authToken)
			throws Exception {
		ClientCashReceiptsReport[] data = accountingService.getClientCashReceipts(clientCashReceipts,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Client Income Summary Report") // label for swagger
	@PostMapping("/dashboard/client-income-summary-report")
	public ResponseEntity<?> getClientIncomeSummary (@RequestBody ClientCashReceipts requestData, @RequestParam String authToken)
			throws Exception {
		ClientIncomeSummaryReport[] data = accountingService.getClientIncomeSummary(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  AR Report") // label for swagger
	@PostMapping("/dashboard/ar-report")
	public ResponseEntity<?> getARReport (@RequestBody SearchAR requestData, @RequestParam String authToken)
			throws Exception {
		ARReport[] data = accountingService.getARReport(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Write Off Report") // label for swagger
	@PostMapping("/dashboard/write-off-report")
	public ResponseEntity<?> getWriteOffReport (@RequestBody ClientCashReceipts requestData, @RequestParam String authToken)
			throws Exception {
		WriteOffReport[] data = accountingService.getWriteOffReport(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Billed Hours Paid Report") // label for swagger
	@PostMapping("/dashboard/billed-hours-paid-report")
	public ResponseEntity<?> getBilledHoursPaidReport (@RequestBody BilledHoursPaid requestData, @RequestParam String authToken)
			throws Exception {
		BilledHoursReport[] data = accountingService.getBilledHoursPaidReport(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Billed Paid Fees Report") // label for swagger
	@PostMapping("/dashboard/billed-paid-fees-report")
	public ResponseEntity<?> getBilledHoursPaidReport (@RequestBody BilledPaidFees requestData, @RequestParam String authToken)
			throws Exception {
		BilledPaidFeesReport[] data = accountingService.getBilledPaidFeesReport(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Dashboard -  Expiration Date Report") // label for swagger
	@PostMapping("/dashboard/expiration-date")
	public ResponseEntity<?> getExpirationDateReport (@RequestBody ExpirationDateRequest requestData, @RequestParam String authToken)
			throws Exception {
		ExpirationDateReport[] data = accountingService.getExpirationDateReport(requestData,authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}