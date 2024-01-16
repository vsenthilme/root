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
import com.mnrclara.wrapper.core.model.accounting.InvoiceCreateResponse;
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
@RequestMapping("/mnr-spark-service")
@Api(tags = {"Spark Service"}, value = "Spark Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Spark Modules")})
public class SparkServiceController { 
	
	@Autowired
	AccountingService accountingService;

	@ApiOperation(response = Optional.class, value = "Spark Test") 
   	@PostMapping("/spark/invoiceHeader")
   	public ResponseEntity<?> findInvoiceHeaders(@RequestBody SearchInvoiceHeader searchInvoiceHeader) throws Exception {
    	com.mnrclara.wrapper.core.model.accounting.spark.InvoiceHeader[] invoiceHeaders = 
    			accountingService.getInvoiceHeaders(searchInvoiceHeader);
   		return new ResponseEntity<>(invoiceHeaders, HttpStatus.OK);
   	}
}