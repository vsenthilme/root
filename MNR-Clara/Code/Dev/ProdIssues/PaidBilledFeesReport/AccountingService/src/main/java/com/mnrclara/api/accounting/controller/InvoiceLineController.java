package com.mnrclara.api.accounting.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.accounting.model.invoice.AddInvoiceLine;
import com.mnrclara.api.accounting.model.invoice.InvoiceLine;
import com.mnrclara.api.accounting.service.InvoiceLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"InvoiceLine"}, value = "InvoiceLine  Operations related to InvoiceLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "InvoiceLine ",description = "Operations related to InvoiceLine ")})
@RequestMapping("/invoiceline")
@RestController
public class InvoiceLineController {
	
	@Autowired
	InvoiceLineService invoicelineService;
	
    @ApiOperation(response = InvoiceLine.class, value = "Get all InvoiceLine details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<InvoiceLine> invoicelineList = invoicelineService.getInvoiceLines();
		return new ResponseEntity<>(invoicelineList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = InvoiceLine.class, value = "Get a InvoiceLine") // label for swagger 
	@GetMapping("/{invoiceNumber}")
	public ResponseEntity<?> getInvoiceLine(@PathVariable String invoiceNumber) {
    	List<InvoiceLine> invoiceline = invoicelineService.getInvoiceLine(invoiceNumber);
    	log.info("InvoiceLine : " + invoiceline);
		return new ResponseEntity<>(invoiceline, HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceLine.class, value = "Create InvoiceLine") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postInvoiceLine(@Valid @RequestBody AddInvoiceLine newInvoiceLine, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		InvoiceLine createdInvoiceLine = invoicelineService.createInvoiceLine(newInvoiceLine, loginUserID);
		return new ResponseEntity<>(createdInvoiceLine , HttpStatus.OK);
	}
    
    @ApiOperation(response = InvoiceLine.class, value = "Delete InvoiceLine") // label for swagger
	@DeleteMapping("/{invoiceFiscalYear}")
	public ResponseEntity<?> deleteInvoiceLine(@PathVariable String invoiceFiscalYear, @RequestParam String loginUserID) {
    	invoicelineService.deleteInvoiceLine(invoiceFiscalYear, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}