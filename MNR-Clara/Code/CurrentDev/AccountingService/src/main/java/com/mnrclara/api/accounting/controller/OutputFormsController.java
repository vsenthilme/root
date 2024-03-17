package com.mnrclara.api.accounting.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.accounting.model.prebill.outputform.PreBillOutputForm;
import com.mnrclara.api.accounting.service.OutputFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"OutputForms"}, value = "OutputForms  Operations related to OutputFormsController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "OutputForms ",description = "Operations related to OutputForms ")})
@RequestMapping("/outputforms")
@RestController
public class OutputFormsController {
	
	@Autowired
	OutputFormService outputFormService;
	
    @ApiOperation(response = PreBillOutputForm.class, value = "Get a PREBILLDETAILS OutputForms") // label for swagger 
	@GetMapping("/preBill")
	public ResponseEntity<?> getPreBillDetails(@RequestParam String preBillNumber, @RequestParam String matterNumber) throws ParseException {
    	PreBillOutputForm preBillOutputForm = outputFormService.getPreBillDetailsOutputForm(preBillNumber, matterNumber);
		return new ResponseEntity<>(preBillOutputForm, HttpStatus.OK); 
	}
    
    @ApiOperation(response = PreBillOutputForm.class, value = "Get a INVOICE OutputForms") // label for swagger 
    @GetMapping("/invoice")
   	public ResponseEntity<?> getInvoiceDetails(@RequestParam String invoiceNumber) {
       	PreBillOutputForm preBillOutputForm = outputFormService.getInvoiceOutputForm(invoiceNumber);
   		return new ResponseEntity<>(preBillOutputForm, HttpStatus.OK);
   	}
}