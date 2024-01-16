package com.mnrclara.api.accounting.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanLineEntity;
import com.mnrclara.api.accounting.service.PaymentPlanLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"PaymentPlanLine"}, value = "PaymentPlanLine  Operations related to PaymentPlanLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PaymentPlanLine ",description = "Operations related to PaymentPlanLine ")})
@RequestMapping("/paymentplanline")
@RestController
public class PaymentPlanLineController {
	
	@Autowired
	PaymentPlanLineService paymentplanlineService;
	
    @ApiOperation(response = PaymentPlanLineEntity.class, value = "Get all PaymentPlanLine details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PaymentPlanLineEntity> paymentplanlineList = paymentplanlineService.getPaymentPlanLines();
		return new ResponseEntity<>(paymentplanlineList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = PaymentPlanLineEntity.class, value = "Get a PaymentPlanLine") // label for swagger 
	@GetMapping("/{paymentPlanNumber}")
	public ResponseEntity<?> getPaymentPlanLine(@PathVariable String paymentPlanNumber, 
			@RequestParam Long paymentPlanRevisionNo, @RequestParam Long itemNumber) {
    	PaymentPlanLineEntity paymentplanline = paymentplanlineService.getPaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber);
    	log.info("PaymentPlanLine : " + paymentplanline);
		return new ResponseEntity<>(paymentplanline, HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentPlanLineEntity.class, value = "Send SMS") // label for swagger
   	@GetMapping("/reminderSMS")
   	public ResponseEntity<?> sendReminderSMS() 
   			throws IllegalAccessException, InvocationTargetException, ParseException {
   		Boolean smsResponse = paymentplanlineService.sendReminderSMS();
   		return new ResponseEntity<>(smsResponse , HttpStatus.OK);
   	}
}